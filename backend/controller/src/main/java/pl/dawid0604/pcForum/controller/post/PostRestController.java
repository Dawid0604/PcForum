package pl.dawid0604.pcForum.controller.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.post.NewPostDTO;
import pl.dawid0604.pcForum.service.post.PostRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostRestController {
    private final PostRestService postRestService;

    @GetMapping("/{encryptedId}")
    public ResponseEntity<?> findPosts(@PathVariable("encryptedId") final String encryptedThreadId,
                                       @RequestParam(required = false, name = "page", defaultValue = "1") final int page,
                                       @RequestParam(required = false, name = "size", defaultValue = "25") final int size) {

        return new ResponseEntity<>(postRestService.findAllByThread(encryptedThreadId, page, size), OK);
    }

    @GetMapping("/newest")
    public ResponseEntity<?> findPosts(@RequestParam(required = false, name = "amount", defaultValue = "3") final int numberOfPosts) {
        return new ResponseEntity<>(postRestService.findNewestPosts(numberOfPosts), OK);
    }

    @ResponseStatus(OK)
    @PostMapping("/create")
    public void create(@RequestBody final NewPostDTO payload) {
        postRestService.create(payload);
    }
}
