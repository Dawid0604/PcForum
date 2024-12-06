package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRepository;
import pl.dawid0604.pcForum.repository.user.UserProfileVisitorRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileVisitorDaoService;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
class UserProfileVisitorDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileVisitorEntity>
                                       implements UserProfileVisitorDaoService {

    private final UserProfileVisitorRepository userProfileVisitorRepository;
    private final UserProfileRepository userProfileRepository;
    private final EncryptionService encryptionService;

    public UserProfileVisitorDaoServiceImpl(final UserProfileVisitorRepository repository, final UserProfileRepository userProfileRepository,
                                            final EncryptionService encryptionService) {
        super(repository);
        this.userProfileVisitorRepository = repository;
        this.userProfileRepository = userProfileRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    public UserProfileVisitorEntity save(UserProfileVisitorEntity entity) {
        if(isNotBlank(entity.getEncryptedId())) {
            return userProfileVisitorRepository.save(entity);
        }

        entity = userProfileVisitorRepository.save(entity);
        entity.setEncryptedId(encryptionService.encryptUserProfileVisitor(entity.getId()));
        return userProfileVisitorRepository.save(entity);
    }

    @Override
    public long countVisitsByUser(final String encryptedUserProfileId) {
        return userProfileVisitorRepository.countVisitsByUser(encryptionService.decryptId(encryptedUserProfileId));
    }

    @Override
    @Transactional
    public void handleProfileView(final String encryptedUserProfileId) {
        UserProfileEntity loggedUser = getLoggedUserProfile();
        UserProfileEntity visitedUserProfile = userProfileRepository.findIdByIdWithoutFields(encryptionService.decryptId(encryptedUserProfileId))
                                                                    .orElseThrow();

        if(Objects.equals(loggedUser.getEncryptedId(), visitedUserProfile.getEncryptedId())) {
            return;
        }

        if(userProfileVisitorRepository.existsProfileVisitById(encryptionService.decryptId(encryptedUserProfileId))) {
            userProfileVisitorRepository.updateLastActivity(encryptionService.decryptId(encryptedUserProfileId), getCurrentDate());

        } else {
            LocalDateTime now = getCurrentDate();
            save(UserProfileVisitorEntity.builder()
                                                 .profile(visitedUserProfile)
                                                 .visitor(loggedUser)
                                                 .firstVisitDate(now)
                                                 .lastVisitDate(now)
                                                 .build());
        }
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
