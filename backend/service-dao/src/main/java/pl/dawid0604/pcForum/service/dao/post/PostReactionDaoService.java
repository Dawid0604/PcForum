package pl.dawid0604.pcForum.service.dao.post;

public interface PostReactionDaoService {
    long countUpVotesByUser(String userEncryptedId);

    long countDownVotesByUser(String userEncryptedId);
}
