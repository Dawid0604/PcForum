package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.user.UserProfileVisitorEntity;
import pl.dawid0604.pcForum.repository.user.UserProfileVisitorRepository;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserProfileVisitorDaoService;

@Service
class UserProfileVisitorDaoServiceImpl extends EntityBaseDaoServiceImpl<UserProfileVisitorEntity>
                                       implements UserProfileVisitorDaoService {

    private final UserProfileVisitorRepository userProfileVisitorRepository;

    public UserProfileVisitorDaoServiceImpl(final UserProfileVisitorRepository repository) {
        super(repository);
        this.userProfileVisitorRepository = repository;
    }
}
