package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.post.PostEntityContent;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.*;
import pl.dawid0604.pcForum.dto.post.ExtendedPostDTO;
import pl.dawid0604.pcForum.dto.post.PostContentDTO;
import pl.dawid0604.pcForum.dto.post.ThreadNewestPostDTO;
import pl.dawid0604.pcForum.dto.thread.ThreadDTO;
import pl.dawid0604.pcForum.dto.user.*;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.dao.session.SpringSessionDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadCategoryDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadDaoService;
import pl.dawid0604.pcForum.service.dao.user.*;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
@RequiredArgsConstructor
class UserProfileRestServiceImpl implements UserProfileRestService {
    private final UserProfileDaoService userProfileDaoService;
    private final UserProfileRankDaoService userProfileRankDaoService;
    private final PostDaoService postDaoService;
    private final ThreadDaoService threadDaoService;
    private final PostReactionDaoService postReactionDaoService;
    private final UserProfileVisitorDaoService userProfileVisitorDaoService;
    private final UserProfileObservationDaoService userProfileObservationDaoService;
    private final UserDaoService userDaoService;
    private final SpringSessionDaoService springSessionDaoService;
    private final ThreadCategoryDaoService threadCategoryDaoService;

    @Override
    public UserProfileDTO getUserProfileBaseInfo() {
        return userProfileDaoService.findBaseInfo(getLoggedUserUsername())
                                    .map(_user -> new UserProfileDTO(_user.getEncryptedId(), _user.getNickname(), _user.getAvatar()))
                                    .orElseThrow();
    }

    @Override
    @Transactional
    public ResponseEntity<?> register(final UserRegistartionDTO payload) {
        if(isBlank(payload.username())) {
            return new ResponseEntity<>("Username cannot be empty", BAD_REQUEST);
        }

        if(isBlank(payload.password())) {
            return new ResponseEntity<>("Password cannot be empty", BAD_REQUEST);
        }

        if(isBlank(payload.nickname())) {
            return new ResponseEntity<>("Nickname cannot be empty", BAD_REQUEST);
        }

        if(userDaoService.existsByUsername(payload.username())) {
            return new ResponseEntity<>("Username already exists", BAD_REQUEST);
        }

        if(userProfileDaoService.existsByNickname(payload.nickname())) {
            return new ResponseEntity<>("Nickname already exists", BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                                    .username(payload.username())
                                    .password(userDaoService.encryptPassword(payload.password()))
                                    .role(UserEntityRole.USER)
                                    .build();

        UserProfileRankEntity defaultRank = userProfileRankDaoService.findDefaultRank()
                                                                     .orElseThrow();

        UserProfileEntity userProfileEntity = UserProfileEntity.builder()
                                                               .nickname(payload.nickname())
                                                               .createdAt(getCurrentDate())
                                                               .user(userDaoService.save(user))
                                                               .rank(defaultRank)
                                                               .build();

        userProfileDaoService.save(userProfileEntity);
        return new ResponseEntity<>(CREATED);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDetailsDTO getUserProfileDetailsInfo(final String encryptedUserProfileId) {
        UserProfileEntity userProfile = userProfileDaoService.findDetailsInfo(encryptedUserProfileId)
                                                             .orElseThrow();

        long numberOfPosts = postDaoService.countPostsByUser(userProfile.getEncryptedId());
        long numberOfThreads = threadDaoService.countThreadsByUser(userProfile.getEncryptedId());
        long numberOfUpVotes = postReactionDaoService.countUpVotesByUser(userProfile.getEncryptedId());
        long numberOfDownVotes = postReactionDaoService.countDownVotesByUser(userProfile.getEncryptedId());
        long numberOfVisits = userProfileVisitorDaoService.countVisitsByUser(userProfile.getEncryptedId());
        long numberOfObservations = userProfileObservationDaoService.countObservationsByUser(userProfile.getEncryptedId());

        boolean isObserved = false;
        boolean isLoggedUser = false;
        var possibleLoggedUser = getLoggedUserProfile();

        if(possibleLoggedUser.isPresent()) {
            var loggedUser = possibleLoggedUser.get();
            isLoggedUser = loggedUser.getEncryptedId().equals(userProfile.getEncryptedId());
            isObserved = userProfileObservationDaoService.isObserved(loggedUser.getEncryptedId(), userProfile.getEncryptedId());
        }

        var activities = mapActivities(userProfile.getEncryptedId());
        return new UserProfileDetailsDTO(userProfile.getEncryptedId(), userProfile.getAvatar(), userProfile.getNickname(), userProfile.getRank().getName(), formatDate(userProfile.getCreatedAt()),
                                         formatDate(userProfile.getLastActivity()), numberOfPosts, numberOfThreads, numberOfUpVotes, numberOfDownVotes, numberOfVisits,
                numberOfObservations, activities, isLoggedUser, userProfile.isOnline(), isObserved);
    }

    @Override
    public UserProfileThreadsDTO findUserProfileThreads(final String encryptedUserProfileId) {
        var userProfile = userProfileDaoService.findNicknameAvatarEncryptedIdById(encryptedUserProfileId)
                                               .orElseThrow();

        var threads = threadDaoService.findThreadsByUser(userProfile.getEncryptedId())
                                      .stream()
                                      .map(this::mapThread)
                                      .toList();

        return new UserProfileThreadsDTO(userProfile.getNickname(), userProfile.getAvatar(), userProfile.getEncryptedId(), threads);
    }

    @Override
    public UserProfilePostsDTO findUserProfilePosts(final String encryptedUserProfileId) {
        var userProfile = userProfileDaoService.findNicknameAvatarEncryptedIdById(encryptedUserProfileId)
                                               .orElseThrow();

        var posts = postDaoService.findPostsByUser(userProfile.getEncryptedId())
                                  .stream()
                                  .map(this::mapPost)
                                  .toList();

        return new UserProfilePostsDTO(userProfile.getNickname(), userProfile.getAvatar(), userProfile.getEncryptedId(), posts);
    }

    @Override
    public UserProfileVisitorsDTO findUserProfileVisitors(final String encryptedUserProfileId) {
        var userProfile = userProfileDaoService.findNicknameAvatarEncryptedIdById(encryptedUserProfileId)
                                               .orElseThrow();

        var visitors = userProfileVisitorDaoService.findVisitorsByUser(userProfile.getEncryptedId())
                                                   .stream()
                                                   .map(this::mapVisitor)
                                                   .toList();

        return new UserProfileVisitorsDTO(userProfile.getNickname(), userProfile.getAvatar(), userProfile.getEncryptedId(), visitors);
    }

    private UserProfileVisitorDTO mapVisitor(final UserProfileVisitorEntity userProfileVisitorEntry) {
        var userProfile = userProfileVisitorEntry.getVisitor();
        return new UserProfileVisitorDTO(userProfile.getEncryptedId(), userProfile.getNickname(), userProfile.getAvatar(),
                          formatDate(userProfileVisitorEntry.getFirstVisitDate()), formatDate(userProfileVisitorEntry.getLastVisitDate()));
    }

    @Transactional(readOnly = true)
    private ThreadDTO mapThread(final ThreadEntity threadEntity) {
        String categoryName = null;

        if(threadEntity.getCategoryLevelPathTwo() != null && threadEntity.getCategoryLevelPathThree() != null) {
            categoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), threadEntity.getCategoryLevelPathTwo(),
                                                                     threadEntity.getCategoryLevelPathThree())
                                                   .orElseThrow();
        }

        return new ThreadDTO(threadEntity.getEncryptedId(), threadEntity.getTitle(), threadEntity.getUserProfile().getEncryptedId(),
                             threadEntity.getUserProfile().getNickname(), threadEntity.getUserProfile().getAvatar(),
                             formatDate(threadEntity.getLastActivity()), categoryName, getNewestPost(threadEntity.getEncryptedId()),
                             threadEntity.getNumberOfViews(), postDaoService.countPostsByThread(threadEntity.getEncryptedId()));
    }

    @Transactional(readOnly = true)
    private ThreadNewestPostDTO getNewestPost(final String encryptedThreadId) {
        return postDaoService.findNewestByThreadId(encryptedThreadId)
                             .map(_post -> new ThreadNewestPostDTO(_post.getEncryptedId(), _post.getUserProfile().getEncryptedId(), _post.getUserProfile().getNickname(),
                                                                   _post.getUserProfile().getAvatar(), formatDate(_post.getCreatedAt())))
                             .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ActivitySummaryDTO getActivitySummary(final ActivitySummarySortType summarySortType, final int numberOfUsers) {
        LocalDateTime timeFrom = switch (summarySortType) {
            case WEEK -> getCurrentDate().minusWeeks(1);
            case MONTH -> getCurrentDate().minusMonths(1);
            case ANNUALLY -> getCurrentDate().minusYears(1);
            case ALL_THE_TIME -> LocalDateTime.of(1, 1, 1, 0, 0);
        };

        var users = postReactionDaoService.findUsersWithMostNumberOfUpVotes(timeFrom, numberOfUsers)
                                          .stream()
                                          .map(_userProfile -> mapProfile(_userProfile, timeFrom))
                                          .toList();

        return new ActivitySummaryDTO(users);
    }

    @Override
    public UsersDTO getNumberOfOnlineUsers() {
        var onlineUsers = springSessionDaoService.findOnlineUsers();

        userProfileDaoService.setAsOnline(onlineUsers);
        return new UsersDTO(userProfileDaoService.count(), onlineUsers.size());
    }

    @Override
    public UserProfileObservationsDTO findUserProfileObservations(final String encryptedUserProfileId) {
        var userProfile = userProfileDaoService.findNicknameAvatarEncryptedIdById(encryptedUserProfileId)
                                               .orElseThrow();

        var observations = userProfileObservationDaoService.findObservationsByUser(userProfile.getEncryptedId())
                                                           .stream()
                                                           .map(this::mapObservation)
                                                           .toList();

        return new UserProfileObservationsDTO(userProfile.getNickname(), userProfile.getAvatar(), userProfile.getEncryptedId(), observations);
    }

    private UserProfileObservationDTO mapObservation(final UserProfileObservationEntity userProfileObservationEntry) {
        var userProfile = userProfileObservationEntry.getProfile();
        return new UserProfileObservationDTO(userProfile.getEncryptedId(), userProfile.getNickname(), userProfile.getAvatar(),
                                             formatDate(userProfileObservationEntry.getObservationDate()));
    }

    @Transactional(readOnly = true)
    private ActivitySummaryDTO.UserDTO mapProfile(final UserProfileEntity userProfile, final LocalDateTime dateFrom) {
        long numberOfUpVotes = postReactionDaoService.countUpVotesByIdWithTimeInterval(userProfile.getEncryptedId(), dateFrom);
        return new ActivitySummaryDTO.UserDTO(userProfile.getEncryptedId(), userProfile.getAvatar(), userProfile.getNickname(), numberOfUpVotes);
    }

    private List<UserProfileDetailsDTO.ActivityDTO> mapActivities(final String encryptedUserProfileId) {
        return emptyList();
    }

    private Optional<UserProfileEntity> getLoggedUserProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String loggedUserUsername =  ((User) authentication.getPrincipal()).getUsername();
            return Optional.of(userProfileDaoService.findByUsername(loggedUserUsername)
                                                    .orElseThrow());
        }

        return Optional.empty();
    }

    private static String getLoggedUserUsername() {
        return ((User) SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getPrincipal()).getUsername();
    }

    private ExtendedPostDTO mapPost(final PostEntity post) {
        var userProfile = new UserProfileDTO(post.getUserProfile().getEncryptedId(), post.getUserProfile().getNickname(), post.getUserProfile().getAvatar());
        var thread = new ExtendedPostDTO.Thread(post.getThread().getEncryptedId(), post.getThread().getTitle());
        var content = mapContent(post.getContent());
        return new ExtendedPostDTO(post.getEncryptedId(), userProfile, formatDate(post.getCreatedAt()), thread, content);
    }

    private List<PostContentDTO> mapContent(final List<PostEntityContent> contentEntities) {
        List<PostContentDTO> groupedContent = new LinkedList<>();

        for(var _contentFragment: contentEntities) {
            if(isNotBlank(_contentFragment.postId())) {
                var blockquotePost = postDaoService.findContentAndUserById(_contentFragment.postId())
                                                   .orElseThrow();

                for(var _blockquoteContent: blockquotePost.getContent()) {
                    var meta = new PostContentDTO.BlockquoteMetaDTO(blockquotePost.getUserProfile().getNickname(), formatDate(blockquotePost.getCreatedAt()));
                    groupedContent.add(new PostContentDTO(_blockquoteContent.content(), meta));
                }
            } groupedContent.add(new PostContentDTO(_contentFragment.content(), null));

        } return groupedContent;
    }
}
