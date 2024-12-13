package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.List;
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

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p SET p.isOnline = true WHERE p.nickname IN :onlineUsers")
    void setAsOnline(List<String> onlineUsers);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} p SET p.isOnline = false WHERE :onlineUsers IS NULL OR p.nickname NOT IN :onlineUsers")
    void setAsOffline(List<String> onlineUsers);
}
