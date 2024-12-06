package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRankRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileRankDaoService;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
class UserProfileRankDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileRankEntity>
                                    implements UserProfileRankDaoService {

    private final UserProfileRankRepository userProfileRankRepository;
    private final EncryptionService encryptionService;

    public UserProfileRankDaoServiceImpl(final UserProfileRankRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.userProfileRankRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserProfileRankEntity> findDefaultRank() {
        return userProfileRankRepository.findDefaultRank();
    }

    @Override
    public UserProfileRankEntity save(UserProfileRankEntity entity) {
        if(isNotBlank(entity.getEncryptedId())) {
            return userProfileRankRepository.save(entity);
        }

        entity = userProfileRankRepository.save(entity);
        entity.setEncryptedId(encryptionService.encryptUserProfileRank(entity.getId()));
        return userProfileRankRepository.save(entity);
    }
}
