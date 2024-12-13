package pl.dawid0604.pcForum.controller.browser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.BrowserResultDTO;
import pl.dawid0604.pcForum.service.browser.BrowserRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/browser")
public class BrowserRestController {
    private final BrowserRestService browserRestService;

    @ResponseStatus(OK)
    @GetMapping("/find")
    public BrowserResultDTO find(@RequestParam(name = "text") final String text,
                                 @RequestParam(required = false, name = "page", defaultValue = "1") final int page,
                                 @RequestParam(required = false, name = "size", defaultValue = "25") final int size) {

        return browserRestService.find(text, page, size);
    }
}
