package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileObservationRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileObservationDaoService;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
class UserProfileObservationDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileObservationEntity>
                                           implements UserProfileObservationDaoService {

    private final UserProfileObservationRepository userProfileObservationRepository;
    private final EncryptionService encryptionService;

    public UserProfileObservationDaoServiceImpl(final UserProfileObservationRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.userProfileObservationRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    public UserProfileObservationEntity save(UserProfileObservationEntity entity) {
        if(isNotBlank(entity.getEncryptedId())) {
            return userProfileObservationRepository.save(entity);
        }

        entity = userProfileObservationRepository.save(entity);
        entity.setEncryptedId(encryptionService.encryptUserProfileObservation(entity.getId()));
        return userProfileObservationRepository.save(entity);
    }

    @Override
    public long countObservationsByUser(final String encryptedUserProfileId) {
        return userProfileObservationRepository.countByUserProfileEncryptedId(encryptionService.decryptId(encryptedUserProfileId));
    }
}
