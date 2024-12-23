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

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
    public long countByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo) {
        return threadRepository.countByCategory(categoryLevelPathOne, categoryLevelPathTwo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadEntity> findDetailsById(final String encryptedThreadId) {
        return threadRepository.findDetailsById(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    @Transactional
    public void incrementThreadViews(final String encryptedThreadId) {
        threadRepository.updateThreadNumberOfViews(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadEntity> findMostPopularThreads(final int numberOfThreads) {
        return threadRepository.findMostPopularThreads(numberOfThreads);
    }

    @Override
    @Transactional
    public ThreadEntity save(ThreadEntity thread) {
        if(isNotBlank(thread.getEncryptedId())) {
            return threadRepository.save(thread);
        }

        thread = threadRepository.save(thread);
        thread.setEncryptedId(encryptionService.encryptThread(thread.getId()));
        return threadRepository.save(thread);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadEntity> findByIdWithoutFields(final String encryptedThreadId) {
        return threadRepository.findByIdWithoutFields(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    public void closeThread(final String encryptedThreadId) {
        threadRepository.setThreadAsClosed(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    public void deleteThread(final String encryptedThreadId) {
        threadRepository.deleteById(encryptionService.decryptId(encryptedThreadId));
    }

    @Override
    public long countThreadsByUser(final String encryptedUserId) {
        return threadRepository.countThreadsByUser(encryptionService.decryptId(encryptedUserId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadEntity> findThreadsByUser(final String encryptedId) {
        return threadRepository.findThreadsByUser(encryptionService.decryptId(encryptedId));
    }
}
