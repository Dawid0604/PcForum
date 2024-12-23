package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileObservationRepository;
import pl.dawid0604.pcForum.repository.user.UserProfileRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileObservationDaoService;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
class UserProfileObservationDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileObservationEntity>
                                           implements UserProfileObservationDaoService {

    private final UserProfileObservationRepository userProfileObservationRepository;
    private final EncryptionService encryptionService;
    private final UserProfileRepository userProfileRepository;

    public UserProfileObservationDaoServiceImpl(final UserProfileObservationRepository repository, final EncryptionService encryptionService,
                                                final UserProfileRepository userProfileRepository) {

        super(repository);
        this.userProfileObservationRepository = repository;
        this.encryptionService = encryptionService;
        this.userProfileRepository = userProfileRepository;
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

    @Override
    public void handleProfileFollow(final String encryptedUserProfileId) {
        UserProfileEntity loggedUser = getLoggedUserProfile();
        UserProfileEntity userProfileToFollow = userProfileRepository.findIdByIdWithoutFields(encryptionService.decryptId(encryptedUserProfileId))
                                                                     .orElseThrow();

        if(Objects.equals(loggedUser.getEncryptedId(), userProfileToFollow.getEncryptedId())) {
            return;
        }

        var possibleFollow = userProfileObservationRepository.findByUsers(encryptionService.decryptId(loggedUser.getEncryptedId()),
                                                                          encryptionService.decryptId(userProfileToFollow.getEncryptedId()));

        if(possibleFollow.isPresent()) {
            userProfileObservationRepository.deleteObservationById(possibleFollow.get().getId());

        } else {
            save(UserProfileObservationEntity.builder()
                                             .profile(loggedUser)
                                             .observedProfile(userProfileToFollow)
                                             .observationDate(getCurrentDate())
                                             .build());
        }
    }

    @Override
    public boolean isObserved(final String encryptedLoggedUserProfileId, final String encryptedUserProfileId) {
        return userProfileObservationRepository.isObserved(encryptionService.decryptId(encryptedLoggedUserProfileId), encryptionService.decryptId(encryptedUserProfileId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileObservationEntity> findObservationsByUser(final String encryptedId) {
        return userProfileObservationRepository.findObservationsByUser(encryptionService.decryptId(encryptedId));
    }

    private UserProfileEntity getLoggedUserProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String loggedUserUsername =  ((User) authentication.getPrincipal()).getUsername();
            return userProfileRepository.findIdByUsername(loggedUserUsername)
                                        .orElseThrow();
        }

        throw new IllegalArgumentException();
    }
}
