package pl.dawid0604.pcForum.service.dao.thread;

import org.springframework.data.domain.Page;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;

import java.util.Optional;

public interface ThreadDaoService {
    Page<ThreadEntity> findAllByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                                         Integer categoryLevelPathThree, int page, int size);

    Optional<ThreadEntity> findNewestByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                                                Integer categoryLevelPathThree);

    long countByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                         Integer categoryLevelPathThree);

    Optional<ThreadEntity> findDetailsById(String encryptedThreadId);
}
