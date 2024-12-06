package pl.dawid0604.pcForum.service.dao.user;

import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.util.Optional;

public interface UserProfileDaoService {
    Optional<UserProfileEntity> findBaseInfo(String username);

    Optional<UserProfileEntity> findByUsername(String username);

    Optional<String> findEncryptedIdByUsername(String username);

    boolean existsByNickname(String nickname);

    UserProfileEntity save(UserProfileEntity userProfile);

    Optional<UserProfileEntity> findByIdWithoutFields(String loggedUserEncryptedId);

    Optional<UserProfileEntity> findDetailsInfo(String userProfileEncryptedId);
}
