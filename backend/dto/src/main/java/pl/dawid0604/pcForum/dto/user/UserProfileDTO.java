package pl.dawid0604.pcForum.dto.user;

public record UserProfileDTO(String encryptedId, String nickname, String avatar, String rank, long numberOfPosts,
                             long numberOfUpVotes, long numberOfDownVotes) { }
