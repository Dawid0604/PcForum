package pl.dawid0604.pcForum.service.impl.browser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.post.PostEntityContent;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dto.BrowserResultDTO;
import pl.dawid0604.pcForum.dto.post.ExtendedPostDTO;
import pl.dawid0604.pcForum.dto.post.PostContentDTO;
import pl.dawid0604.pcForum.dto.post.ThreadNewestPostDTO;
import pl.dawid0604.pcForum.dto.thread.ThreadDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.service.browser.BrowserRestService;
import pl.dawid0604.pcForum.service.dao.browser.BrowserDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadCategoryDaoService;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;

@Service
@RequiredArgsConstructor
public class BrowserRestServiceImpl implements BrowserRestService {
    private final BrowserDaoService browserDaoService;
    private final PostDaoService postDaoService;
    private final ThreadCategoryDaoService threadCategoryDaoService;

    @Override
    @Transactional(readOnly = true)
    public BrowserResultDTO find(final String text, int page, final int size) {
        page = (page > 1) ? (page - 1) : 0;
        var foundThreads = browserDaoService.findThreadsByText(text, page, size)
                                            .stream()
                                            .map(this::mapThread)
                                            .toList();

        var foundUserProfiles = browserDaoService.findUserProfilesByText(text, page, size)
                                                 .stream()
                                                 .map(this::mapUserProfile)
                                                 .toList();

        var foundPosts = browserDaoService.findPostsByText(text, page, size)
                                          .stream()
                                          .map(this::mapPost)
                                          .toList();

        return new BrowserResultDTO(foundThreads, foundPosts, foundUserProfiles);
    }

    private ThreadDTO mapThread(final ThreadEntity thread) {
        return new ThreadDTO(thread.getEncryptedId(), thread.getTitle(), thread.getUserProfile().getEncryptedId(),
                             thread.getUserProfile().getNickname(), thread.getUserProfile().getAvatar(),
                             formatDate(thread.getLastActivity()), getCategoryName(thread), getNewestPost(thread.getEncryptedId()),
                             thread.getNumberOfViews(), postDaoService.countPostsByThread(thread.getEncryptedId()));
    }

    @Transactional(readOnly = true)
    private ThreadNewestPostDTO getNewestPost(final String encryptedThreadId) {
        return postDaoService.findNewestByThreadId(encryptedThreadId)
                             .map(_post -> new ThreadNewestPostDTO(_post.getEncryptedId(), _post.getUserProfile().getEncryptedId(), _post.getUserProfile().getNickname(),
                                                                   _post.getUserProfile().getAvatar(), formatDate(_post.getCreatedAt())))
                             .orElse(null);
    }

    @Transactional(readOnly = true)
    private String getCategoryName(final ThreadEntity threadEntity) {
        String subCategoryName;
        String categoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), threadEntity.getCategoryLevelPathTwo(), null)
                                                      .orElseThrow();

        if(threadEntity.getCategoryLevelPathTwo() != null) {
            subCategoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), threadEntity.getCategoryLevelPathTwo(), threadEntity.getCategoryLevelPathThree())
                                                      .orElseThrow();

            categoryName += ", " + subCategoryName;
        }

        return categoryName;
    }

    private UserProfileDTO mapUserProfile(final UserProfileEntity userProfile) {
        return new UserProfileDTO(userProfile.getEncryptedId(), userProfile.getNickname(), userProfile.getAvatar(), userProfile.getRank().getName());
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
