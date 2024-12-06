package pl.dawid0604.pcForum.repository.post.custom;

import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PostReactionRepositoryCustom {
    List<UserProfileEntity> findUsersWithMostNumberOfUpVotes(LocalDateTime timeFrom, int numberOfUsers);
}
