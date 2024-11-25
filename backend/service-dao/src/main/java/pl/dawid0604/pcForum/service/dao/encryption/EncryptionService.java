package pl.dawid0604.pcForum.service.dao.encryption;

public interface EncryptionService {
    long decryptId(String encryptedId);

    String encryptUser(long userId);

    String encryptUserProfile(long profileId);

    String encryptUserProfileObservation(long profileObservationId);

    String encryptUserProfileVisitor(long profileVisitorId);

    String encryptUserProfileRank(long rankId);

    String encryptThread(long threadId);

    String encryptThreadCategory(long threadCategoryId);

    String encryptPost(long postId);

    String encryptPostReaction(long postReactionId);
}
