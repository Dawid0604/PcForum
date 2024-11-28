package pl.dawid0604.pcForum.service.dao.user;

import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.util.Optional;

public interface UserProfileDaoService {
    Optional<String> findNicknameById(String encryptedUserProfileId);

    Optional<UserProfileEntity> findBaseInfo(String username);

    Optional<UserProfileEntity> findByUsername(String username);

    boolean existsByNickname(String nickname);

    UserProfileEntity save(UserProfileEntity userProfile);
}
