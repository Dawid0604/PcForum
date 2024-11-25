package pl.dawid0604.pcForum.dto.thread;

import java.util.List;

public record GroupedThreadCategoryDTO(
        String encryptedId,
        String name,
        long numberOfRows,
        List<ThreadCategoryDTO> subCategories,
        ThreadDTO newestThread) { }
