package pl.dawid0604.pcForum.dto;

import pl.dawid0604.pcForum.dto.post.ExtendedPostDTO;
import pl.dawid0604.pcForum.dto.thread.ThreadDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;

import java.util.List;

public record BrowserResultDTO(List<ThreadDTO> foundThreads, List<ExtendedPostDTO> foundPosts,
                               List<UserProfileDTO> foundUserProfiles) { }
