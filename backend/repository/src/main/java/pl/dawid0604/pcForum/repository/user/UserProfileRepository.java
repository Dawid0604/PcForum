package pl.dawid0604.pcForum.repository.user;

import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

@Repository
public interface UserProfileRepository extends EntityBaseRepository<UserProfileEntity> { }
