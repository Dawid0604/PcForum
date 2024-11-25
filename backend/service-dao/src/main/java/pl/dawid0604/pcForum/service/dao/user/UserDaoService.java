package pl.dawid0604.pcForum.service.dao.user;

import pl.dawid0604.pcForum.dao.user.UserEntity;

import java.util.Optional;

public interface UserDaoService {
    Optional<UserEntity> findByUsername(String username);
}
