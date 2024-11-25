package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.user.UserProfileVisitorDaoService;
import pl.dawid0604.pcForum.service.user.UserProfileVisitorRestService;

@Service
@RequiredArgsConstructor
class UserProfileVisitorRestServiceImpl implements UserProfileVisitorRestService {
    private final UserProfileVisitorDaoService userProfileVisitorDaoService;
}
