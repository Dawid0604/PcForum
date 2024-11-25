package pl.dawid0604.pcForum.repository.thread.custom;

import pl.dawid0604.pcForum.dao.thread.ThreadEntity;

import java.util.Optional;

public interface ThreadRepositoryCustom {
    Optional<ThreadEntity> findNewestByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                                                Integer categoryLevelPathThree);
}
