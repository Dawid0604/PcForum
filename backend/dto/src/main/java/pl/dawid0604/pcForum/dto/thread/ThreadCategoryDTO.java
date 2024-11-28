package pl.dawid0604.pcForum.dto.thread;

import java.util.List;

public record ThreadCategoryDTO(
    String encryptedId,
    String name,
    String description,
    long numberOfThreads,
    long numberOfPosts,
    String thumbnailPath,
    List<ThreadCategoryDTO> subCategories,
    ThreadDTO newestThread) { }
