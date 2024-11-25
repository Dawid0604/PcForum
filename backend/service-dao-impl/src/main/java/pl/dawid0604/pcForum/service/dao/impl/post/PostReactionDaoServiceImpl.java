package pl.dawid0604.pcForum.service.dao.impl.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.repository.post.PostReactionRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;

@Service
class PostReactionDaoServiceImpl extends EntityBaseDaoServiceImpl<PostReactionEntity>
                                 implements PostReactionDaoService {

    private final PostReactionRepository postReactionRepository;
    private final EncryptionService encryptionService;

    public PostReactionDaoServiceImpl(final PostReactionRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.postReactionRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public long countUpVotesByUser(final String userEncryptedId) {
        return postReactionRepository.countUpVotesByUser(encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countDownVotesByUser(final String userEncryptedId) {
        return postReactionRepository.countDownVotesByUser(encryptionService.decryptId(userEncryptedId));
    }
}
