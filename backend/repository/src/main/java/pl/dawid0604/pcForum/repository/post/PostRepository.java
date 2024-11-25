package pl.dawid0604.pcForum.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.repository.post.custom.PostRepositoryCustom;

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
}
