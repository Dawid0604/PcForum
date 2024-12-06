package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserEntity;
import pl.dawid0604.pcForum.dao.user.UserEntityRole;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.dto.user.ActivitySummaryDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDetailsDTO;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadDaoService;
import pl.dawid0604.pcForum.service.dao.user.*;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;
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
        long numberOfFollowers = userProfileObservationDaoService.countObservationsByUser(userProfile.getEncryptedId());

        boolean isLoggedUser = isLoggedUser(userProfile.getEncryptedId());
        var activities = mapActivities(userProfile.getEncryptedId());

        return new UserProfileDetailsDTO(userProfile.getAvatar(), userProfile.getNickname(), userProfile.getRank().getName(), formatDate(userProfile.getCreatedAt()),
                                        formatDate(userProfile.getLastActivity()), numberOfPosts, numberOfThreads, numberOfUpVotes, numberOfDownVotes, numberOfVisits,
                                        numberOfFollowers, activities, isLoggedUser, userProfile.isOnline());
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

    @Transactional(readOnly = true)
    private ActivitySummaryDTO.UserDTO mapProfile(final UserProfileEntity userProfile, final LocalDateTime dateFrom) {
        long numberOfUpVotes = postReactionDaoService.countUpVotesByIdWithTimeInterval(userProfile.getEncryptedId(), dateFrom);
        return new ActivitySummaryDTO.UserDTO(userProfile.getEncryptedId(), userProfile.getAvatar(), userProfile.getNickname(), numberOfUpVotes);
    }

    private List<UserProfileDetailsDTO.ActivityDTO> mapActivities(final String encryptedUserProfileId) {
        return emptyList();
    }

    private boolean isLoggedUser(final String searchedUserProfileEncryptedId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            var loggedUserUsername = ((User) authentication.getPrincipal())
                                                           .getUsername();

            return userProfileDaoService.findEncryptedIdByUsername(loggedUserUsername)
                                        .map(_userProfile -> _userProfile.equals(searchedUserProfileEncryptedId))
                                        .orElseThrow();
        } return false;
    }

    private static String getLoggedUserUsername() {
        return ((User) SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getPrincipal()).getUsername();
    }
}
