package pl.dawid0604.pcForum.service.dao.user;

import pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity;

import java.util.List;

public interface UserProfileVisitorDaoService {
    long countVisitsByUser(String encryptedUserProfileId);

    void handleProfileView(String encryptedUserProfileId);

    List<UserProfileVisitorEntity> findVisitorsByUser(String encryptedUserProfileId);
}
