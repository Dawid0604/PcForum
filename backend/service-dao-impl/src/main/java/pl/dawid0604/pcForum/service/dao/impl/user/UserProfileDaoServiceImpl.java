package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

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
    public Optional<UserProfileEntity> findBaseInfo(final String username) {
        return userProfileRepository.findBaseInfoByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findByUsername(final String username) {
        return userProfileRepository.findIdByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findEncryptedIdByUsername(final String username) {
        return userProfileRepository.findEncryptedIdByUsername(username);
    }

    @Override
    public boolean existsByNickname(final String nickname) {
        return userProfileRepository.existsByNicknameIgnoreCase(nickname);
    }

    @Override
    @Transactional
    public UserProfileEntity save(UserProfileEntity user) {
        if(isNotBlank(user.getEncryptedId())) {
            return userProfileRepository.save(user);
        }

        user = userProfileRepository.save(user);
        user.setEncryptedId(encryptionService.encryptUserProfile(user.getId()));
        return userProfileRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findByIdWithoutFields(final String loggedUserEncryptedId) {
        return userProfileRepository.findIdByIdWithoutFields(encryptionService.decryptId(loggedUserEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findDetailsInfo(final String userProfileEncryptedId) {
        return userProfileRepository.findDetailsInfoByUsername(encryptionService.decryptId(userProfileEncryptedId));
    }

    @Override
    @Transactional
    public void setAsOnline(final List<String> onlineUsers) {
        userProfileRepository.setAsOnline(onlineUsers, getCurrentDate());
        userProfileRepository.setAsOffline(onlineUsers);
    }

    @Override
    public long count() {
        return userProfileRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileEntity> findNicknameAvatarEncryptedIdById(final String encryptedUserProfileId) {
        return userProfileRepository.findNicknameAvatarEncryptedIdById(encryptionService.decryptId(encryptedUserProfileId));
    }
}
