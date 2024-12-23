package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileObservationRepository extends EntityBaseRepository<UserProfileObservationEntity> {

    @Query("""
           SELECT COUNT(v)
           FROM #{#entityName} v
           WHERE v.observedProfile.id = :userId
           """)
    long countByUserProfileEncryptedId(long userId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity(o.id)
            FROM #{#entityName} o
            WHERE o.profile.id = :loggedUserId AND
                  o.observedProfile.id = :observedUserId
           """)
    Optional<UserProfileObservationEntity> findByUsers(long loggedUserId, long observedUserId);

    @Modifying
    @Transactional
    @Query("DELETE FROM #{#entityName} o WHERE o.id = :id")
    void deleteObservationById(long id);

    @Query("""
            SELECT COUNT(o) > 0
            FROM #{#entityName} o
            WHERE o.profile.id = :loggedUserId AND
                  o.observedProfile.id = :observedUserId
           """)
    boolean isObserved(long loggedUserId, long observedUserId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity(o.encryptedId,
                   new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.encryptedId, p.avatar, p.nickname),
                   o.observationDate)
            FROM #{#entityName} o
            LEFT JOIN o.profile p
            WHERE o.observedProfile.id = :userId
           """)
    List<UserProfileObservationEntity> findObservationsByUser(long userId);
}
