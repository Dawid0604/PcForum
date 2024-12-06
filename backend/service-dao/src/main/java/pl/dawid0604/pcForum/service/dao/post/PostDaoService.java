package pl.dawid0604.pcForum.service.dao.post;

import org.springframework.data.domain.Page;
import pl.dawid0604.pcForum.dao.post.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostDaoService {
    Page<PostEntity> findAllByThreadId(String encryptedThreadId, int page, int size);

    Optional<PostEntity> findNewestByThreadId(String encryptedThreadId);

    long countPostsByUser(String userEncryptedId);

    long countPostsByThread(String threadEncryptedId);

    List<PostEntity> findNewestPosts(int numberOfPosts);

    long countPostsByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);

    PostEntity save(PostEntity postEntity);

    Optional<PostEntity> findByIdWithoutFields(String postEncryptedId);

    Optional<PostEntity> findContentAndUserById(String postEncryptedId);
}
