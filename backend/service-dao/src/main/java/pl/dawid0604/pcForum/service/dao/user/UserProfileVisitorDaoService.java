package pl.dawid0604.pcForum.service.dao.user;

public interface UserProfileVisitorDaoService {
    long countVisitsByUser(String encryptedUserProfileId);

    void handleProfileView(String encryptedUserProfileId);
}
