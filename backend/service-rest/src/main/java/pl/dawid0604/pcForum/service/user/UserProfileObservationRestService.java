package pl.dawid0604.pcForum.service.user;

public interface UserProfileObservationRestService {
    void handleProfileFollow(String encryptedUserProfileId);
}
