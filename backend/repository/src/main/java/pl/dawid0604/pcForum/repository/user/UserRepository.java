package pl.dawid0604.pcForum.repository.user;

import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends EntityBaseRepository<UserEntity> {
    Optional<UserEntity> findByUsername(String username);
}
