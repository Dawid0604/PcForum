package pl.dawid0604.pcForum.service.browser;

import pl.dawid0604.pcForum.dto.BrowserResultDTO;

public interface BrowserRestService {
    BrowserResultDTO find(String text, int page, int size);
}
