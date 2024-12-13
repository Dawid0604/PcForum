package pl.dawid0604.pcForum.dto.post;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

import java.util.List;

public record ExtendedPostDTO(String encryptedId, UserProfileDTO user, String createdAt,
                              Thread thread, List<PostContentDTO> content) {

    public record Thread(String encryptedId, String title) { }
}
