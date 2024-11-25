package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.user.UserProfileRankDaoService;
import pl.dawid0604.pcForum.service.user.UserProfileRankRestService;

@Service
@RequiredArgsConstructor
class UserProfileRankRestServiceImpl implements UserProfileRankRestService {
    private final UserProfileRankDaoService userProfileRankDaoService;
}
