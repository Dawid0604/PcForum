package pl.dawid0604.pcForum.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.repository.post.custom.PostRepositoryCustom;

import java.util.Optional;

@Repository
public interface PostRepository extends EntityBaseRepository<PostEntity>, PostRepositoryCustom {

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.post.PostEntity(p.encryptedId, p.content, p.createdAt, p.lastUpdatedAt,
                   new pl.dawid0604.pcForum.dao.user.UserProfileEntity(u.encryptedId, u.avatar, u.nickname,
                   new pl.dawid0604.pcForum.dao.user.UserProfileRankEntity(r.name)))
            FROM #{#entityName} p
            LEFT JOIN p.userProfile u
            LEFT JOIN u.rank r
            LEFT JOIN p.thread t
            WHERE t.id = :threadId
           """)
    Page<PostEntity> findAllByThreadId(long threadId, Pageable pageable);

    @Query("""
            SELECT COUNT(p)
            FROM #{#entityName} p
            LEFT JOIN p.userProfile u
            WHERE u.id = :userId
           """)
    long countByUserId(long userId);

    @Query("""
            SELECT COUNT(p)
            FROM #{#entityName} p
            LEFT JOIN p.thread t
            WHERE t.id = :threadId
           """)
    long countByThreadId(long threadId);

    @Query("""
            SELECT COUNT(p)
            FROM #{#entityName} p
            LEFT JOIN p.thread t
            WHERE t.categoryLevelPathOne = :categoryLevelPathOne AND
                  (t.categoryLevelPathTwo IS NULL OR t.categoryLevelPathTwo = :categoryLevelPathTwo) AND
                  (:categoryLevelPathThree IS NULL OR t.categoryLevelPathThree = :categoryLevelPathThree)
           """)
    long countByCategory(int categoryLevelPathOne, Integer categoryLevelPathTwo, Integer categoryLevelPathThree);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.post.PostEntity(p.id)
            FROM #{#entityName} p
            WHERE p.id = :postId
           """)
    Optional<PostEntity> findByIdWithoutFields(long postId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.post.PostEntity(p.content, new pl.dawid0604.pcForum.dao.user.UserProfileEntity(u.nickname),
                                                                p.createdAt)
            FROM #{#entityName} p
            LEFT JOIN p.userProfile u
            WHERE p.id = :postId
           """)
    Optional<PostEntity> findContentById(long postId);
}
