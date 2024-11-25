package pl.dawid0604.pcForum.dto.post;

public record ThreadNewestPostDTO(String encryptedId, String authorEncryptedId,
                                  String authorNickname, String authorAvatar, String createdAt) { }
