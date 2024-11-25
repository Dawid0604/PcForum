package pl.dawid0604.pcForum.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.post.PostReactionRestService;

@Service
@RequiredArgsConstructor
class PostReactionRestServiceImpl implements PostReactionRestService {
    private final PostReactionDaoService postReactionDaoService;
}
