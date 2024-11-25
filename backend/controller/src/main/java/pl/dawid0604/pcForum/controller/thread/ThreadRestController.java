package pl.dawid0604.pcForum.controller.thread;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.thread.NewThreadDTO;
import pl.dawid0604.pcForum.service.thread.ThreadRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ThreadRestController {
    private final ThreadRestService threadRestService;

    @GetMapping("/threads/categories")
    public ResponseEntity<?> findThreadCategories() {
        return new ResponseEntity<>(threadRestService.findThreadCategories(), OK);
    }

    @GetMapping("/threads/categories/{encryptedId}/sub")
    public ResponseEntity<?> findThreadSubCategories(@PathVariable("encryptedId") final String encryptedParentCategoryId) {
        return new ResponseEntity<>(threadRestService.findThreadSubCategories(encryptedParentCategoryId), OK);
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

    @PostMapping("/threads/create")
    public ResponseEntity<?> create(@RequestBody final NewThreadDTO payload) {
        return new ResponseEntity<>(threadRestService.create(payload));
    }
}
