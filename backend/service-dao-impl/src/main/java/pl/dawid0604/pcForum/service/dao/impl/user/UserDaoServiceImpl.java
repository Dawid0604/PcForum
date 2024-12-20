package pl.dawid0604.pcForum.service.dao.impl.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserEntity;
import pl.dawid0604.pcForum.repository.user.UserRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.user.UserDaoService;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
class UserDaoServiceImpl extends EntityBaseDaoServiceImpl<UserEntity>
                         implements UserDaoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    public UserDaoServiceImpl(final UserRepository repository, final PasswordEncoder passwordEncoder,
                              final EncryptionService encryptionService) {

        super(repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.encryptionService = encryptionService;
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

    @Override
    @Transactional
    public UserEntity save(UserEntity user) {
        if(isNotBlank(user.getEncryptedId())) {
            return userRepository.save(user);
        }

        user = userRepository.save(user);
        user.setEncryptedId(encryptionService.encryptUser(user.getId()));
        return userRepository.save(user);
    }

    @Override
    public String encryptPassword(final String password) {
        return passwordEncoder.encode(password);
    }
}
