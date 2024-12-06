package pl.dawid0604.pcForum.dto.user;

import java.util.List;

public record ActivitySummaryDTO(List<UserDTO> users) {
    public record UserDTO(String encryptedId, String avatar, String nickname, long numberOfUpVotes) { }
}
