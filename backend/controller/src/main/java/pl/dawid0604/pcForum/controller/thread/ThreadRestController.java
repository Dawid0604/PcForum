package pl.dawid0604.pcForum.controller.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.thread.NewThreadDTO;
import pl.dawid0604.pcForum.service.thread.ThreadRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ThreadRestController {
    private final ThreadRestService threadRestService;

    @GetMapping("/threads/categories")
    public ResponseEntity<?> findThreadCategories() {
        return new ResponseEntity<>(threadRestService.findThreadCategories(), OK);
    }

    @GetMapping("/threads/creator/categories")
    public ResponseEntity<?> findCreatorThreadCategories() {
        return new ResponseEntity<>(threadRestService.findCreatorThreadCategories(), OK);
    }

    @GetMapping("/threads/categories/{encryptedId}/sub")
    public ResponseEntity<?> findThreadSubCategories(@PathVariable("encryptedId") final String encryptedParentCategoryId) {
        return new ResponseEntity<>(threadRestService.findThreadSubCategories(encryptedParentCategoryId), OK);
    }

    @GetMapping("/threads/creator/categories/{encryptedId}/sub")
    public ResponseEntity<?> findCreatorThreadSubCategories(@PathVariable("encryptedId") final String encryptedParentCategoryId) {
        return new ResponseEntity<>(threadRestService.findCreatorThreadCategorySubCategories(encryptedParentCategoryId), OK);
    }

    @GetMapping("/threads/{encryptedId}")
    public ResponseEntity<?> findThreads(@PathVariable("encryptedId") final String encryptedCategoryId,
                                         @RequestParam(required = false, name = "page", defaultValue = "1") final int page,
                                         @RequestParam(required = false, name = "size", defaultValue = "25") final int size) {

        return new ResponseEntity<>(threadRestService.findAllByCategoryPath(encryptedCategoryId, page, size), OK);
    }

    @GetMapping("/thread/{encryptedId}")
    public ResponseEntity<?> findThread(@PathVariable("encryptedId") final String encryptedCategoryId) {
        return new ResponseEntity<>(threadRestService.findThreadDetails(encryptedCategoryId), OK);
    }

    @GetMapping("/threads/popular")
    public ResponseEntity<?> findMostPopularThreads(@RequestParam(required = false, name = "amount", defaultValue = "3") final int numberOfThreads) {
        return new ResponseEntity<>(threadRestService.findMostPopularThreads(numberOfThreads), OK);
    }

    @PostMapping("/thread/create")
    public ResponseEntity<?> create(@RequestBody final NewThreadDTO payload) {
        return new ResponseEntity<>(threadRestService.create(payload), OK);
    }

    @ResponseStatus(OK)
    @PatchMapping("/thread/handle/view/{encryptedId}")
    public void handleThreadView(@PathVariable("encryptedId") final String encryptedThreadId) {
        threadRestService.handleThreadView(encryptedThreadId);
    }
}