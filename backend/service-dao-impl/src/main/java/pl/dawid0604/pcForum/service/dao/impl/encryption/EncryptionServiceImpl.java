package pl.dawid0604.pcForum.service.dao.impl.encryption;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.utils.exception.DecryptionException;
import pl.dawid0604.pcForum.utils.exception.EncryptionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Service
class EncryptionServiceImpl implements EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String USER_SUFFIX = "USR";
    private static final String USER_PROFILE_SUFFIX = "USRP";
    private static final String USER_PROFILE_OBSERVATION_SUFFIX = "USRPO";
    private static final String USER_PROFILE_VISITOR_SUFFIX = "USPRV";
    private static final String USER_PROFILE_RANK_SUFFIX = "USRPR";
    private static final String THREAD_SUFFIX = "THR";
    private static final String THREAD_CATEGORY_SUFFIX = "THRC";
    private static final String POST_SUFFIX = "PST";
    private static final String POST_REACTION_SUFFIX = "PSTR";
    private static final String SEPARATOR = "&";

    private final SecretKeySpec secretKeySpec;
    private final Cipher cipher;

    public EncryptionServiceImpl(@Value("${custom.security.aes.secretKey}") final String secretKey) throws NoSuchPaddingException,
                                                                                                           NoSuchAlgorithmException {
        this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        this.cipher = Cipher.getInstance(ALGORITHM);
    }

    @Override
    public long decryptId(final String encryptedId) {
        return decrypt(encryptedId);
    }

    @Override
    public String encryptUser(final long userId) {
        return encrypt(appendSuffix(userId, USER_SUFFIX));
    }

    @Override
    public String encryptUserProfile(final long profileId) {
        return encrypt(appendSuffix(profileId, USER_PROFILE_SUFFIX));
    }

    @Override
    public String encryptUserProfileObservation(final long profileObservationId) {
        return encrypt(appendSuffix(profileObservationId, USER_PROFILE_OBSERVATION_SUFFIX));
    }

    @Override
    public String encryptUserProfileVisitor(final long profileVisitorId) {
        return encrypt(appendSuffix(profileVisitorId, USER_PROFILE_VISITOR_SUFFIX));
    }

    @Override
    public String encryptUserProfileRank(final long rankId) {
        return encrypt(appendSuffix(rankId, USER_PROFILE_RANK_SUFFIX));
    }

    @Override
    public String encryptThread(final long threadId) {
        return encrypt(appendSuffix(threadId, THREAD_SUFFIX));
    }

    @Override
    public String encryptThreadCategory(final long threadCategoryId) {
        return encrypt(appendSuffix(threadCategoryId, THREAD_CATEGORY_SUFFIX));
    }

    @Override
    public String encryptPost(final long postId) {
        return encrypt(appendSuffix(postId, POST_SUFFIX));
    }

    @Override
    public String encryptPostReaction(final long postReactionId) {
        return encrypt(appendSuffix(postReactionId, POST_REACTION_SUFFIX));
    }

    private String encrypt(final String idWithPrefix) throws EncryptionException {
        try {
            cipher.init(ENCRYPT_MODE, secretKeySpec);

            return Base64.getUrlEncoder()
                         .encodeToString(cipher.doFinal(idWithPrefix.getBytes()));

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new EncryptionException();
        }
    }

    private long decrypt(final String encryptedId) throws DecryptionException {
        try {
            cipher.init(DECRYPT_MODE, secretKeySpec);

            return Long.parseLong(removeSuffix(new String(cipher.doFinal(Base64.getUrlDecoder()
                                                                                                           .decode(encryptedId)))));

        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new DecryptionException();
        }
    }

    private static String appendSuffix(final long id, final String suffix) {
        return id + SEPARATOR + suffix;
    }

    private static String removeSuffix(final String decodedId) {
        return decodedId.substring(0, decodedId.indexOf(SEPARATOR));
    }
}
