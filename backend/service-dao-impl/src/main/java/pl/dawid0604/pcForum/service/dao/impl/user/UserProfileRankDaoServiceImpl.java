package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileRankRepository;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileRankDaoService;

@Service
class UserProfileRankDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileRankEntity>
                                    implements UserProfileRankDaoService {

    private final UserProfileRankRepository userProfileRankRepository;

    public UserProfileRankDaoServiceImpl(final UserProfileRankRepository repository) {
        super(repository);
        this.userProfileRankRepository = repository;
    }
}
