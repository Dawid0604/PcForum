package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserEntity;
import pl.dawid0604.pcForum.repository.user.UserRepository;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserDaoService;

import java.util.Optional;

@Service
class UserDaoServiceImpl extends EntityBaseDaoServiceImpl<UserEntity>
                         implements UserDaoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDaoServiceImpl(final UserRepository repository, final PasswordEncoder passwordEncoder) {
        super(repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEntity> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }
}
