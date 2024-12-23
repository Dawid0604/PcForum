package pl.dawid0604.pcForum.dto.user;

import pl.dawid0604.pcForum.dto.post.ExtendedPostDTO;

import java.util.List;

public record UserProfilePostsDTO(String nickname, String avatar, String encryptedId,
                                  List<ExtendedPostDTO> posts) { }
