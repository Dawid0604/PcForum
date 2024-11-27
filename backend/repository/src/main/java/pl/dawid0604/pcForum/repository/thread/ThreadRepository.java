package pl.dawid0604.pcForum.repository.thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.repository.thread.custom.ThreadRepositoryCustom;

import java.util.Optional;

@Repository
public interface ThreadRepository extends EntityBaseRepository<ThreadEntity>, ThreadRepositoryCustom {

    @Query("""
            SELECT COUNT(t)
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  (:categoryLevelPathTwo IS NULL OR t.categoryLevelPathTwo = :categoryLevelPathTwo) AND
                  (:categoryLevelPathThree IS NULL OR t.categoryLevelPathThree = :categoryLevelPathThree)
           """)
    long countByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                         Integer categoryLevelPathThree);

    @Query("""
            SELECT COUNT(t)
            FROM #{#entityName} t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  (t.categoryLevelPathTwo IS NULL OR :categoryLevelPathTwo IS NULL OR t.categoryLevelPathTwo = :categoryLevelPathTwo)
           """)
    long countByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadEntity(t.encryptedId, t.title, t.createdAt, t.lastActivity,
                   new pl.dawid0604.pcForum.dao.user.UserProfileEntity(u.encryptedId, u.avatar, u.nickname), t.categoryLevelPathOne,
                   t.categoryLevelPathTwo, t.categoryLevelPathThree, t.numberOfViews)
            FROM #{#entityName} t
            LEFT JOIN t.userProfile u
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  (t.categoryLevelPathTwo IS NULL OR t.categoryLevelPathTwo = :categoryLevelPathTwo) AND
                  (:categoryLevelPathThree IS NULL OR t.categoryLevelPathThree = :categoryLevelPathThree)
           """)
    Page<ThreadEntity> findAllByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo,
                                         Integer categoryLevelPathThree, Pageable pageable);


    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.thread.ThreadEntity(t.encryptedId, t.title, t.content, t.createdAt,
                   t.lastUpdatedAt, t.lastActivity, t.isClosed, t.categoryLevelPathOne, t.categoryLevelPathTwo, t.categoryLevelPathThree,
                   new pl.dawid0604.pcForum.dao.user.UserProfileEntity(u.encryptedId, u.avatar, u.nickname,
                   new pl.dawid0604.pcForum.dao.user.UserProfileRankEntity(r.name)))
            FROM #{#entityName} t
            LEFT JOIN t.userProfile u
            LEFT JOIN u.rank r
            WHERE t.id = :threadId
           """)
    Optional<ThreadEntity> findDetailsById(long threadId);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} t SET t.numberOfViews = t.numberOfViews + 1 WHERE t.id = :threadId")
    void updateThreadNumberOfViews(long threadId);
}
