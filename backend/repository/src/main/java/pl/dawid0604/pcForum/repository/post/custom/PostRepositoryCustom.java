package pl.dawid0604.pcForum.repository.post.custom;

import pl.dawid0604.pcForum.dao.post.PostEntity;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<PostEntity> findNewestByThreadId(long threadId);
}