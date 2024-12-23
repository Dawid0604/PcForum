package pl.dawid0604.pcForum.dto.user;

import java.util.List;

public record UserProfileVisitorsDTO(String nickname, String avatar, String encryptedId,
                                     List<UserProfileVisitorDTO> visitors) { }
