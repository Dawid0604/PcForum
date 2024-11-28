package pl.dawid0604.pcForum.service.dao.impl.thread;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity;
import pl.dawid0604.pcForum.repository.thread.ThreadCategoryRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.thread.ThreadCategoryDaoService;

import java.util.List;
import java.util.Optional;

@Service
class ThreadCategoryDaoServiceImpl extends EntityBaseDaoServiceImpl<ThreadCategoryEntity>
                                   implements ThreadCategoryDaoService {

    private final ThreadCategoryRepository threadCategoryRepository;
    private final EncryptionService encryptionService;

    public ThreadCategoryDaoServiceImpl(final ThreadCategoryRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.threadCategoryRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadCategoryEntity> findAll() {
        return threadCategoryRepository.findAllCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadCategoryEntity> findById(final String encryptedThreadCategoryId) {
        return threadCategoryRepository.findCategoryById(encryptionService.decryptId(encryptedThreadCategoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadCategoryEntity> findCategoryPathById(final String encryptedThreadCategoryId) {
        return threadCategoryRepository.findCategoryPathById(encryptionService.decryptId(encryptedThreadCategoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadCategoryEntity> findAllByPathStartsWith(final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                                              final Integer categoryLevelPathThree) {

        return threadCategoryRepository.findAllByPathStartsWith(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadCategoryEntity> findPathById(final String encryptedCategoryId) {
        return threadCategoryRepository.findCategoryById(encryptionService.decryptId(encryptedCategoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findCategoryName(final int categoryLevelPathOne, final Integer categoryLevelPathTwo, final Integer categoryLevelPathThree) {
        return threadCategoryRepository.findCategoryName(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadCategoryEntity> findAllCreatorCategories() {
        return threadCategoryRepository.findAllCreatorCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadCategoryEntity> findAllCreatorCategorySubCategories(final int categoryLevelPathOne, final Integer categoryLevelPathTwo) {
        return threadCategoryRepository.findAllCreatorCategorySubCategories(categoryLevelPathOne, categoryLevelPathTwo);
    }
}
