package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;

import java.util.Optional;

@Service
class UserProfileDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileEntity>
                                implements UserProfileDaoService {

    private final UserProfileRepository userProfileRepository;
    private final EncryptionService encryptionService;

    public UserProfileDaoServiceImpl(final UserProfileRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.userProfileRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findNicknameById(final String encryptedUserProfileId) {
        return userProfileRepository.findNicknameById(encryptionService.decryptId(encryptedUserProfileId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findBaseInfo(final String username) {
        return userProfileRepository.findBaseInfoByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findByUsername(final String username) {
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public boolean existsByNickname(final String nickname) {
        return userProfileRepository.existsByNicknameIgnoreCase(nickname);
    }
}
