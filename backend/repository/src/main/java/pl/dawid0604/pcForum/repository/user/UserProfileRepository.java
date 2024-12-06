package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends EntityBaseRepository<UserProfileEntity> {

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.encryptedId, p.avatar, p.nickname)
            FROM #{#entityName} p
            LEFT JOIN p.user u
            WHERE u.username = :username
           """)
    Optional<UserProfileEntity> findBaseInfoByUsername(String username);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.id, p.encryptedId)
            FROM #{#entityName} p
            WHERE p.user.username = :username
           """)
    Optional<UserProfileEntity> findIdByUsername(String username);

    boolean existsByNicknameIgnoreCase(String nickname);

    @Query("""
            SELECT p.encryptedId
            FROM #{#entityName} p
            WHERE p.user.username = :username
           """)
    Optional<String> findEncryptedIdByUsername(String username);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.id, p.encryptedId)
            FROM #{#entityName} p
            WHERE p.id = :userId
           """)
    Optional<UserProfileEntity> findIdByIdWithoutFields(long userId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileEntity(p.encryptedId, p.avatar, p.createdAt, p.nickname,
                            p.lastActivity, p.isOnline, new pl.dawid0604.pcForum.dao.user.UserProfileRankEntity(r.name))
            FROM #{#entityName} p
            LEFT JOIN p.user u
            LEFT JOIN p.rank r
            WHERE p.id = :userId
           """)
    Optional<UserProfileEntity> findDetailsInfoByUsername(long userId);
}
