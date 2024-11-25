package pl.dawid0604.pcForum.service.dao.impl.thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.repository.thread.ThreadRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.thread.ThreadDaoService;

import java.util.Optional;

@Service
class ThreadDaoServiceImpl extends EntityBaseDaoServiceImpl<ThreadEntity>
                           implements ThreadDaoService {

    private final ThreadRepository threadRepository;
    private final EncryptionService encryptionService;

    public ThreadDaoServiceImpl(final ThreadRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.threadRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThreadEntity> findAllByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                                final Integer categoryLevelPathThree, int page, final int size) {

        if(page >= 1) {
            page = page - 1;
        }

        return threadRepository.findAllByCategory(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree,
                                                  PageRequest.of(page, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadEntity> findNewestByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                                       final Integer categoryLevelPathThree) {

        return threadRepository.findNewestByCategory(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                final Integer categoryLevelPathThree) {

        return threadRepository.countByCategory(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadEntity> findDetailsById(final String encryptedThreadId) {
        return threadRepository.findDetailsById(encryptionService.decryptId(encryptedThreadId));
    }
}
