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

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.encryptedId, p.avatar, p.nickname)
            FROM #{#entityName} p
            LEFT JOIN p.user u
            WHERE u.username = :username
           """)
    Optional<UserProfileEntity> findBaseInfoByUsername(String username);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.id)
            FROM #{#entityName} p
            WHERE p.user.username = :username
           """)
    Optional<UserProfileEntity> findByUsername(String username);

    boolean existsByNicknameIgnoreCase(String nickname);
}
