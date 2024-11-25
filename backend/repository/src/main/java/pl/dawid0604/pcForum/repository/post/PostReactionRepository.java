package pl.dawid0604.pcForum.repository.post;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;

@Repository
public interface PostReactionRepository extends EntityBaseRepository<PostReactionEntity> {

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            LEFT JOIN r.userProfile u
            WHERE r.upVote = true AND
                  u.id = :userId
           """)
    long countUpVotesByUser(long userId);

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            LEFT JOIN r.userProfile u
            WHERE r.downVote = true AND
                  u.id = :userId
           """)
    long countDownVotesByUser(long userId);
}
