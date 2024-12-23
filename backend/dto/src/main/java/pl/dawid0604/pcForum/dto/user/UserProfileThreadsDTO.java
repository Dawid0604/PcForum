package pl.dawid0604.pcForum.dto.user;

import pl.dawid0604.pcForum.dto.thread.ThreadDTO;

import java.util.List;

public record UserProfileThreadsDTO(String nickname, String avatar, String encryptedId,
                                    List<ThreadDTO> threads) { }
