package pl.dawid0604.pcForum.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.post.PostEntityContent;
import pl.dawid0604.pcForum.dto.post.NewPostDTO;
import pl.dawid0604.pcForum.dto.post.NewestPostDTO;
import pl.dawid0604.pcForum.dto.post.PostContentDTO;
import pl.dawid0604.pcForum.dto.post.PostDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadDaoService;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;
import pl.dawid0604.pcForum.service.post.PostRestService;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
@RequiredArgsConstructor
class PostRestServiceImpl implements PostRestService {
    private final PostDaoService postDaoService;
    private final PostReactionDaoService postReactionDaoService;
    private final UserProfileDaoService userProfileDaoService;
    private final ThreadDaoService threadDaoService;

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAllByThread(final String encryptedThreadId, final int page, final int size) {
        var foundThreads = postDaoService.findAllByThreadId(encryptedThreadId, page, size);
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(foundThreads.isEmpty() || authentication instanceof AnonymousAuthenticationToken) {
            return foundThreads.map(_post -> mapPost(_post, null));

        } else {
            var loggedUserUsername = ((User) authentication.getPrincipal())
                                                           .getUsername();

            String encryptedId = userProfileDaoService.findEncryptedIdByUsername(loggedUserUsername)
                                                      .orElseThrow();

            return foundThreads.map(_post -> mapPost(_post, encryptedId));
        }
    }

    @Override
    public List<NewestPostDTO> findNewestPosts(final int numberOfPosts) {
        return postDaoService.findNewestPosts(numberOfPosts)
                             .stream()
                             .map(this::mapNewestPost)
                             .toList();
    }

    @Override
    @Transactional
    public void create(final NewPostDTO payload) {
        if(isBlank(payload.content())) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if(isBlank(payload.threadEncryptedId())) {
            throw new IllegalArgumentException("Thread id cannot be empty");
        }

        var loggedUserUsername = ((User) SecurityContextHolder.getContext().getAuthentication()
                                                                           .getPrincipal())
                                                                           .getUsername();

        var loggedUserProfile = userProfileDaoService.findByUsername(loggedUserUsername)
                                                     .orElseThrow();

        var thread = threadDaoService.findByIdWithoutFields(payload.threadEncryptedId())
                                     .orElseThrow();

        var post = PostEntity.builder()
                             .createdAt(getCurrentDate())
                             .userProfile(loggedUserProfile)
                             .thread(thread)
                             .content(List.of(new PostEntityContent(payload.content(), null)))
                             .build();

        postDaoService.save(post);
    }

    private NewestPostDTO mapNewestPost(final PostEntity postEntity) {
        var user = new UserProfileDTO(postEntity.getUserProfile().getEncryptedId(), postEntity.getUserProfile().getNickname(),
                                      postEntity.getUserProfile().getAvatar());

        var thread = new NewestPostDTO.Thread(postEntity.getThread().getEncryptedId(), postEntity.getThread().getTitle());
        return new NewestPostDTO(postEntity.getEncryptedId(), user, formatDate(postEntity.getCreatedAt()), thread);
    }

    @Transactional(readOnly = true)
    private PostDTO mapPost(final PostEntity postEntity, final String userEncryptedId) {
        UserProfileDTO postAuthor = new UserProfileDTO(postEntity.getUserProfile().getEncryptedId(), postEntity.getUserProfile().getNickname(),
                                                       postEntity.getUserProfile().getAvatar(), postEntity.getUserProfile().getRank().getName(),
                                                       postDaoService.countPostsByUser(postEntity.getUserProfile().getEncryptedId()),
                                                       postReactionDaoService.countUpVotesByUser(postEntity.getUserProfile().getEncryptedId()),
                                                       postReactionDaoService.countDownVotesByUser(postEntity.getUserProfile().getEncryptedId()));

        boolean isLoggedUserPost = postEntity.getUserProfile().getEncryptedId().equals(userEncryptedId);
        long numberOfUpVotes = postReactionDaoService.countUpVotesById(postEntity.getEncryptedId());
        long numberOfDownVotes = postReactionDaoService.countDownVotesById(postEntity.getEncryptedId());

        boolean loggedUserHasUpVote = false;
        boolean loggedUserHasDownVote = false;

        if(isNotBlank(userEncryptedId)) {
            loggedUserHasUpVote = postReactionDaoService.userHasUpVote(postEntity.getEncryptedId(), userEncryptedId);
            loggedUserHasDownVote = postReactionDaoService.userHasDownVote(postEntity.getEncryptedId(), userEncryptedId);
        }

        return new PostDTO(postEntity.getEncryptedId(), postAuthor, mapContent(postEntity.getContent()), formatDate(postEntity.getCreatedAt()),
                           formatDate(postEntity.getLastUpdatedAt()), isLoggedUserPost, numberOfUpVotes, numberOfDownVotes,
                           loggedUserHasUpVote, loggedUserHasDownVote);
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
