package pl.dawid0604.pcForum.service.dao.user;


import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;

import java.util.Optional;

public interface UserProfileRankDaoService {
    Optional<UserProfileRankEntity> findDefaultRank();
}
