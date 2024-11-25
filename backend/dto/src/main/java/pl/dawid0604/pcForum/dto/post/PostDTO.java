package pl.dawid0604.pcForum.dto.post;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

public record PostDTO(String encryptedId, UserProfileDTO user,
                      String content, String createdAt, String lastUpdatedAt) { }
