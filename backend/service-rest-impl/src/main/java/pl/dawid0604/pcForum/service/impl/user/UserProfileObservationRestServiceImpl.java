package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.user.UserProfileObservationDaoService;
import pl.dawid0604.pcForum.service.user.UserProfileObservationRestService;

@Service
@RequiredArgsConstructor
class UserProfileObservationRestServiceImpl implements UserProfileObservationRestService {
    private final UserProfileObservationDaoService userProfileObservationDaoService;
}
