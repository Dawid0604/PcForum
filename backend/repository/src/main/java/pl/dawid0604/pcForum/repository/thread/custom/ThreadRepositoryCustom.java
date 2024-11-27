package pl.dawid0604.pcForum.repository.thread.custom;

import pl.dawid0604.pcForum.dao.thread.ThreadEntity;

import java.util.List;
import java.util.Optional;

public interface ThreadRepositoryCustom {
    Optional<ThreadEntity> findNewestByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                                                Integer categoryLevelPathThree);

    List<ThreadEntity> findMostPopularThreads(int numberOfThreads);
}
