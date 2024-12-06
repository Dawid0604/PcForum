package pl.dawid0604.pcForum.service.user;

public interface UserProfileVisitorRestService {
    void handleProfileView(String encryptedUserProfileId);
}
