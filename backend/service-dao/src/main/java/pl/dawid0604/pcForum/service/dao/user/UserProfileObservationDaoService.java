package pl.dawid0604.pcForum.service.dao.user;

import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;

import java.util.List;

public interface UserProfileObservationDaoService {
    long countObservationsByUser(String encryptedUserProfileId);

    void handleProfileFollow(String encryptedUserProfileId);

    boolean isObserved(String encryptedLoggedUserProfileId, String encryptedUserProfileId);

    List<UserProfileObservationEntity> findObservationsByUser(String encryptedId);
}
