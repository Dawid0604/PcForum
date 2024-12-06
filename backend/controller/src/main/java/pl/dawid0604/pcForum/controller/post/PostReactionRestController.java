package pl.dawid0604.pcForum.controller.post;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.service.post.PostReactionRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/reaction")
public class PostReactionRestController {
    private final PostReactionRestService postReactionRestService;

    @ResponseStatus(OK)
    @PatchMapping("/up_vote/{encryptedId}")
    public void handleUpVote(@PathVariable("encryptedId") final String encryptedPostId) {
        postReactionRestService.handleUpVote(encryptedPostId);
    }

    @ResponseStatus(OK)
    @PatchMapping("/down_vote/{encryptedId}")
    public void handleDownVote(@PathVariable("encryptedId") final String encryptedPostId) {
        postReactionRestService.handleDownVote(encryptedPostId);
    }
}
