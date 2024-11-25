package pl.dawid0604.pcForum.service.post;

import org.springframework.data.domain.Page;
import pl.dawid0604.pcForum.dto.post.PostDTO;

public interface PostRestService {
    Page<PostDTO> findAllByThread(String encryptedThreadId, int page, int size);
}
