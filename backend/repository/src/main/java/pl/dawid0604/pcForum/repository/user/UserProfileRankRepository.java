package pl.dawid0604.pcForum.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

import java.util.Optional;

@Repository
public interface UserProfileRankRepository extends EntityBaseRepository<UserProfileRankEntity> {

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.user.UserProfileRankEntity(r.id)
            FROM #{#entityName} r
            WHERE r.minPoints = 0
           """)
    Optional<UserProfileRankEntity> findDefaultRank();
}
