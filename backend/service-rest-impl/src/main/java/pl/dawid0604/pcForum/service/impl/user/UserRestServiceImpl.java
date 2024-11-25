package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.user.UserDaoService;
import pl.dawid0604.pcForum.service.user.UserRestService;

@Service
@RequiredArgsConstructor
class UserRestServiceImpl implements UserRestService {
    private final UserDaoService userDaoService;
}
