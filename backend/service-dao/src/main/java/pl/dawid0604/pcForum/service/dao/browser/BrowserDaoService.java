package pl.dawid0604.pcForum.service.dao.browser;

import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.util.List;

public interface BrowserDaoService {
    List<ThreadEntity> findThreadsByText(String text, int page, int size);

    List<PostEntity> findPostsByText(String text, int page, int size);

    List<UserProfileEntity> findUserProfilesByText(String text, int page, int size);
}
