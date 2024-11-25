package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRepository;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;

@Service
class UserProfileDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileEntity>
                                implements UserProfileDaoService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileDaoServiceImpl(final UserProfileRepository repository) {
        super(repository);
        this.userProfileRepository = repository;
    }
}
