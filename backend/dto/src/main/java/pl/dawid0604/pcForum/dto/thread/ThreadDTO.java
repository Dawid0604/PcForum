package pl.dawid0604.pcForum.dto.thread;

import pl.dawid0604.pcForum.dto.post.ThreadNewestPostDTO;

public record ThreadDTO(String encryptedId, String title, String authorEncryptedId,
                        String authorNickname, String authorAvatar, String lastActivityDate,
                        String categoryName, ThreadNewestPostDTO newestPost) { }
