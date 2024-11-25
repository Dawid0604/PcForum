package pl.dawid0604.pcForum.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dto.post.PostDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.post.PostRestService;

import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;

@Service
@RequiredArgsConstructor
class PostRestServiceImpl implements PostRestService {
    private final PostDaoService postDaoService;
    private final PostReactionDaoService postReactionDaoService;

    @Override
    @Transactional(readOnly = true)
    public Page<PostDTO> findAllByThread(final String encryptedThreadId, final int page, final int size) {
        return postDaoService.findAllByThreadId(encryptedThreadId, page, size)
                             .map(this::mapPost);
    }

    @Transactional(readOnly = true)
    private PostDTO mapPost(final PostEntity postEntity) {
        UserProfileDTO postAuthor = new UserProfileDTO(postEntity.getUserProfile().getEncryptedId(), postEntity.getUserProfile().getNickname(),
                                                       postEntity.getUserProfile().getAvatar(), postEntity.getUserProfile().getRank().getName(),
                                                       postDaoService.countPostsByUser(postEntity.getUserProfile().getEncryptedId()),
                                                       postReactionDaoService.countUpVotesByUser(postEntity.getUserProfile().getEncryptedId()),
                                                       postReactionDaoService.countDownVotesByUser(postEntity.getUserProfile().getEncryptedId()));

        return new PostDTO(postEntity.getEncryptedId(), postAuthor, postEntity.getContent(),
                           formatDate(postEntity.getCreatedAt()), formatDate(postEntity.getLastUpdatedAt()));
    } 
}
