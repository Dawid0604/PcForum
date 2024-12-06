package pl.dawid0604.pcForum.service.post;

public interface PostReactionRestService {
    void handleUpVote(String postEncryptedId);

    void handleDownVote(String postEncryptedId);
}
