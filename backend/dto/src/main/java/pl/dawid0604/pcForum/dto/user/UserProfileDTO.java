package pl.dawid0604.pcForum.dto.user;

public record UserProfileDTO(String encryptedId, String nickname, String avatar, String rank, long numberOfPosts,
                             long numberOfUpVotes, long numberOfDownVotes) {

    public UserProfileDTO(final String encryptedId, final String nickname, final String avatar) {
        this(encryptedId, nickname, avatar, null, 0, 0, 0);
    }

    public UserProfileDTO(final String encryptedId, final String nickname,
                          final String avatar, final String rank) {

        this(encryptedId, nickname, avatar, rank, 0, 0, 0);
    }
}
