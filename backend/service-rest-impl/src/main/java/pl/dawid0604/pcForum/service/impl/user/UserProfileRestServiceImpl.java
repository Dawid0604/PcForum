package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;

@Service
@RequiredArgsConstructor
class UserProfileRestServiceImpl implements UserProfileRestService {
    private final UserProfileDaoService userProfileDaoService;
}
