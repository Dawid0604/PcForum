package pl.dawid0604.pcForum.dto.thread;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

public record MostPopularThreadDTO(String encryptedId, String title, UserProfileDTO user, String createdAt, long numberOfPosts) { }
