package pl.dawid0604.pcForum.service.dao.post;

import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostReactionDaoService {
    long countUpVotesByUser(String userEncryptedId);

    long countDownVotesByUser(String userEncryptedId);

    long countUpVotesById(String postEncryptedId);

    long countDownVotesById(String postEncryptedId);

    boolean userHasUpVote(String postEncryptedId, String userEncryptedId);

    boolean userHasDownVote(String postEncryptedId, String userEncryptedId);

    Optional<PostReactionEntity> findByPostAndUser(String postEncryptedId, String loggedUserEncryptedId);

    PostReactionEntity save(PostReactionEntity postReactionEntity);

    void deleteById(String encryptedId);

    void updateStatuses(String encryptedId, boolean upVote, boolean downVote);

    List<UserProfileEntity> findUsersWithMostNumberOfUpVotes(LocalDateTime timeFrom, int numberOfUsers);

    long countUpVotesByIdWithTimeInterval(String encryptedId, LocalDateTime dateFrom);
}
