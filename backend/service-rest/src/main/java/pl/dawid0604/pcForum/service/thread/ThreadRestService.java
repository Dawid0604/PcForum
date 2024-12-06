package pl.dawid0604.pcForum.service.thread;

import org.springframework.data.domain.Page;
import pl.dawid0604.pcForum.dto.thread.*;

import java.util.List;

public interface ThreadRestService {
    List<GroupedThreadCategoryDTO> findThreadCategories();

    List<CreatorThreadCategoryDTO> findCreatorThreadCategories();

    List<CreatorThreadCategoryDTO.ThreadSubCategory> findCreatorThreadCategorySubCategories(String encryptedCategoryId);

    Page<ThreadDTO> findAllByCategoryPath(String encryptedCategoryId, int page, int size);

    GroupedThreadCategoryDTO findThreadSubCategories(String encryptedParentCategoryId);

    NewThreadResponseDTO create(NewThreadDTO payload);

    ThreadDetailsDTO findThreadDetails(String encryptedThreadId);

    void handleThreadView(String encryptedThreadId);

    List<MostPopularThreadDTO> findMostPopularThreads(int size);

    void closeThread(String encryptedThreadId);

    void deleteThread(String encryptedThreadId);
}
