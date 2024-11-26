package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends EntityBaseRepository<UserProfileEntity> {

    @Query("""
            SELECT u.nickname
            FROM #{#entityName} u
            WHERE u.id = :userProfileId
           """)
    Optional<String> findNicknameById(long userProfileId);
}
