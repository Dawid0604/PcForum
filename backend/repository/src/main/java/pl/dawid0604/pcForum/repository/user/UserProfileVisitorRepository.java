package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserProfileVisitorRepository extends EntityBaseRepository<UserProfileVisitorEntity> {

    @Query("""
           SELECT COUNT(v)
           FROM #{#entityName} v
           WHERE v.profile.id = :userId
           """)
    long countVisitsByUser(long userId);

    @Modifying
    @Transactional
    @Query("""
        UPDATE #{#entityName} v SET v.lastVisitDate = :currentDate
        WHERE v.profile.id = :userId
    """)
    void updateLastActivity(long userId, LocalDateTime currentDate);

    @Query("""
            SELECT COUNT(v) > 0
            FROM #{#entityName} v
            WHERE v.profile.id = :viewedUserId AND
                  v.visitor.id = :loggedUserId
            """)
    boolean existsVisitByUsers(long loggedUserId, long viewedUserId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity(
                   new pl.dawid0604.pcForum.dao.user.UserProfileEntity(u.encryptedId, u.avatar, u.nickname),
                   v.firstVisitDate, v.lastVisitDate)
            FROM #{#entityName} v
            LEFT JOIN v.visitor u
            WHERE v.profile.id = :userId
           """)
    List<UserProfileVisitorEntity> findVisitorsByUser(long userId);
}
