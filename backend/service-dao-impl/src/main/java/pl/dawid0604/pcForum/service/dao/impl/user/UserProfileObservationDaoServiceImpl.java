package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.user.UserProfileObservationEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileObservationRepository;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileObservationDaoService;

@Service
class UserProfileObservationDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileObservationEntity>
                                           implements UserProfileObservationDaoService {

    private final UserProfileObservationRepository userProfileObservationRepository;

    public UserProfileObservationDaoServiceImpl(final UserProfileObservationRepository repository) {
        super(repository);
        this.userProfileObservationRepository = repository;
    }
}
