package pl.dawid0604.pcForum.dto.post;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

public record NewestPostDTO(String encryptedId, UserProfileDTO user, String createdAt,
                            Thread thread) {

    public record Thread(String encryptedId, String title) { }
}
