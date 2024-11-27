package pl.dawid0604.pcForum.service.thread;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import pl.dawid0604.pcForum.dto.thread.*;

import java.util.List;

public interface ThreadRestService {
    List<GroupedThreadCategoryDTO> findThreadCategories();

    Page<ThreadDTO> findAllByCategoryPath(String encryptedCategoryId, int page, int size);

    GroupedThreadCategoryDTO findThreadSubCategories(String encryptedParentCategoryId);

    HttpStatus create(NewThreadDTO payload);

    ThreadDetailsDTO findThreadDetails(String encryptedThreadId);

    void handleThreadView(String encryptedThreadId);

    List<MostPopularThreadDTO> findMostPopularThreads(int size);
}
