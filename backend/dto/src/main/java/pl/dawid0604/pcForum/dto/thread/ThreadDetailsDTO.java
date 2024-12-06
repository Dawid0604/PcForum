package pl.dawid0604.pcForum.dto.thread;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

public record ThreadDetailsDTO(String encryptedId, String title, String content, String createdAt,
                               String lastUpdatedAt, String lastActivity, boolean isClosed,
                               UserProfileDTO user, String categoryName, String subCategoryName,
                               boolean loggedUserThread) { }
