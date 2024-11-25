package pl.dawid0604.pcForum.service.dao.thread;

import pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface ThreadCategoryDaoService {
    List<ThreadCategoryEntity> findAll();

    Optional<ThreadCategoryEntity> findById(String encryptedId);

    List<ThreadCategoryEntity> findAllByPathStartsWith(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);

    Optional<ThreadCategoryEntity> findPathById(String encryptedCategoryId);

    Optional<String> findCategoryName(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);
}
