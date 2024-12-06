package pl.dawid0604.pcForum.repository.post;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.repository.post.custom.PostReactionRepositoryCustom;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostReactionRepository extends EntityBaseRepository<PostReactionEntity>, PostReactionRepositoryCustom {

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            LEFT JOIN r.userProfile u
            LEFT JOIN r.post p
            WHERE r.upVote = true AND
                  p.userProfile.id = :userId
           """)
    long countUpVotesByUser(long userId);

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            LEFT JOIN r.userProfile u
            LEFT JOIN r.post p
            WHERE r.downVote = true AND
                  p.userProfile.id = :userId
           """)
    long countDownVotesByUser(long userId);

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            WHERE r.post.id = :postId AND
                  r.upVote = true
           """)
    long countUpVotesById(long postId);

    @Query("""
            SELECT COUNT(r)
            FROM #{#entityName} r
            WHERE r.post.id = :postId AND
                  r.downVote = true
           """)
    long countDownVotesById(long postId);

    @Query("""
            SELECT COUNT(r) > 0
            FROM #{#entityName} r
            WHERE r.post.id = :postId AND
                  r.userProfile.id = :userId AND
                  r.upVote = true
           """)
    boolean userHasUpVote(long postId, long userId);

    @Query("""
            SELECT COUNT(r) > 0
            FROM #{#entityName} r
            WHERE r.post.id = :postId AND
                  r.userProfile.id = :userId AND
                  r.downVote = true
           """)
    boolean userHasDownVote(long postId, long userId);

    @Query("""
            SELECT new pl.dawid0604.pcForum.dao.post.PostReactionEntity(r.encryptedId, r.upVote, r.downVote)
            FROM #{#entityName} r
            WHERE r.post.id = :postId AND
                  r.userProfile.id = :userId
           """)
    Optional<PostReactionEntity> findByPostAndUser(long postId, long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM #{#entityName} r WHERE r.id = :reactionId")
    void deleteReactionById(long reactionId);

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} r SET r.upVote = :upVote, r.downVote = :downVote WHERE r.post.id = :postId")
    void updateStatuses(long postId, boolean upVote, boolean downVote);

    @Query("""
            SELECT COALESCE(SUM(r.upVote), 0)
            FROM #{#entityName} r
            LEFT JOIN r.post p
            WHERE r.upVote = true AND
                  p.userProfile.id = :userId AND
                  r.createdAt >= :dateFrom
           """)
    long countUpVotesByIdWithTimeInterval(long userId, LocalDateTime dateFrom);
}
