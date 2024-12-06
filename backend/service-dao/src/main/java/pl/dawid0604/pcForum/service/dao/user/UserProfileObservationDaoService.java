package pl.dawid0604.pcForum.service.dao.user;

public interface UserProfileObservationDaoService {
    long countObservationsByUser(String encryptedUserProfileId);
}
