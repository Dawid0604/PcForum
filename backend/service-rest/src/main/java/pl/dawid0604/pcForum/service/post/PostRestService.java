package pl.dawid0604.pcForum.service.post;

import org.springframework.data.domain.Page;
import pl.dawid0604.pcForum.dto.post.NewPostDTO;
import pl.dawid0604.pcForum.dto.post.NewestPostDTO;
import pl.dawid0604.pcForum.dto.post.PostDTO;

import java.util.List;

public interface PostRestService {
    Page<PostDTO> findAllByThread(String encryptedThreadId, int page, int size);

    List<NewestPostDTO> findNewestPosts(int numberOfPosts);

    void create(NewPostDTO payload);
}
