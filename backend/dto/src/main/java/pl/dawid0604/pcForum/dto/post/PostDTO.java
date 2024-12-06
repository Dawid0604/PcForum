package pl.dawid0604.pcForum.dto.post;

import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

import java.util.List;

public record PostDTO(String encryptedId, UserProfileDTO user,
                      List<PostContentDTO> content, String createdAt, String lastUpdatedAt,
                      boolean loggedUserPost, long numberOfUpVotes, long numberOfDownVotes,
                      boolean loggedUserHasUpVote, boolean loggedUserHasDownVote) { }
