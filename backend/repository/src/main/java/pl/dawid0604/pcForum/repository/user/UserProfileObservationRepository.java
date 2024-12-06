package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

@Repository
public interface UserProfileObservationRepository extends EntityBaseRepository<UserProfileObservationEntity> {

    @Query("""
           SELECT COUNT(v)
           FROM #{#entityName} v
           WHERE v.profile.id = :userId
           """)
    long countByUserProfileEncryptedId(long userId);
}
