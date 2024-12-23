package pl.dawid0604.pcForum.service.dao.impl.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.repository.post.PostRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
class PostDaoServiceImpl extends EntityBaseDaoServiceImpl<PostEntity>
                         implements PostDaoService {

    private final PostRepository postRepository;
    private final EncryptionService encryptionService;

    public PostDaoServiceImpl(final PostRepository repository,
                              final EncryptionService encryptionService) {

        super(repository);
        this.postRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostEntity> findAllByThreadId(final String encryptedThreadId, int page, final int size) {
        if(page >= 1) {
            page = page - 1;
        }

        return postRepository.findAllByThreadId(encryptionService.decryptId(encryptedThreadId), PageRequest.of(page, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostEntity> findNewestByThreadId(final String encryptedThreadId) {
        return postRepository.findNewestByThreadId(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countPostsByUser(final String userEncryptedId) {
        return postRepository.countByUserId(encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countPostsByThread(final String threadEncryptedId) {
        return postRepository.countByThreadId(encryptionService.decryptId(threadEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostEntity> findNewestPosts(final int numberOfPosts) {
        return postRepository.findNewestPosts(numberOfPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPostsByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo, final Integer categoryLevelPathThree) {
        return postRepository.countByCategory(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree);
    }

    @Override
    @Transactional
    public PostEntity save(PostEntity entity) {
        if(isNotBlank(entity.getEncryptedId())) {
            return postRepository.save(entity);
        }

        entity = postRepository.save(entity);
        entity.setEncryptedId(encryptionService.encryptPost(entity.getId()));
        return postRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostEntity> findByIdWithoutFields(final String postEncryptedId) {
        return postRepository.findByIdWithoutFields(encryptionService.decryptId(postEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostEntity> findContentAndUserById(final String postEncryptedId) {
        return postRepository.findContentById(encryptionService.decryptId(postEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostEntity> findPostsByUser(final String encryptedId) {
        return postRepository.findPostsByUser(encryptionService.decryptId(encryptedId));
    }
}
