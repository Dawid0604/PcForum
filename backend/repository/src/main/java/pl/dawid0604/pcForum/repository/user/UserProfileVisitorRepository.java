package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.time.LocalDateTime;

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
            WHERE v.profile.id = :userId
            """)
    boolean existsProfileVisitById(long userId);
}
