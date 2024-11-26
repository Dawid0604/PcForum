package pl.dawid0604.pcForum.service.dao.user;

import java.util.Optional;

public interface UserProfileDaoService {
    Optional<String> findNicknameById(String encryptedUserProfileId);
}
