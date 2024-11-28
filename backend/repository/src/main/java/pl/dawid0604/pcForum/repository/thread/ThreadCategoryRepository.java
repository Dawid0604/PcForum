package pl.dawid0604.pcForum.repository.thread;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreadCategoryRepository extends EntityBaseRepository<ThreadCategoryEntity> {

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.encryptedId, t.name, t.description, t.thumbnailPath,
                                                                            t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree)
            FROM #{#entityName} t
           """)
    List<ThreadCategoryEntity> findAllCategories();

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.encryptedId, t.name, t.description, t.thumbnailPath,
                                                                            t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree)
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  t.categoryLevelPathTwo = :categoryLevelPathTwo AND
                  t.categoryLevelPathThree IS NOT NULL           AND
                  :categoryLevelPathThree IS NULL
           """)
    List<ThreadCategoryEntity> findAllByPathStartsWith(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.encryptedId, t.name, t.description, t.thumbnailPath,
                                                                            t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree)
            FROM #{#entityName} t
            WHERE t.id = :threadCategoryId
           """)
    Optional<ThreadCategoryEntity> findCategoryById(long threadCategoryId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree)
            FROM #{#entityName} t
            WHERE t.id = :threadCategoryId
           """)
    Optional<ThreadCategoryEntity> findCategoryPathById(long threadCategoryId);

    @Query("""
            SELECT t.name
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  ((:categoryLevelPathTwo IS NULL AND t.categoryLevelPathTwo IS NULL) OR t.categoryLevelPathTwo = :categoryLevelPathTwo) AND
                  ((:categoryLevelPathThree IS NULL AND t.categoryLevelPathThree IS NULL) OR t.categoryLevelPathThree = :categoryLevelPathThree)
           """)
    Optional<String> findCategoryName(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.encryptedId, t.name, t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree)
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne IS NOT NULL AND
                  t.categoryLevelPathThree IS NULL
           """)
    List<ThreadCategoryEntity> findAllCreatorCategories();

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity(t.encryptedId, t.name)
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  t.categoryLevelPathTwo = :categoryLevelPathTwo AND
                  t.categoryLevelPathThree IS NOT NULL
           """)
    List<ThreadCategoryEntity> findAllCreatorCategorySubCategories(int categoryLevelPathOne, Integer categoryLevelPathTwo);
}
