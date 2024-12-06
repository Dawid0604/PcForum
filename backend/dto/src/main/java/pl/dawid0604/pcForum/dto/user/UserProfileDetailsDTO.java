package pl.dawid0604.pcForum.dto.user;

import java.util.List;

public record UserProfileDetailsDTO(String avatar, String nickname, String rank, String createdAt, String lastActivity,
                                    long numberOfPosts, long numberOfThreads, long numberOfUpVotes, long numberOfDownVotes,
                                    long numberOfVisits, long numberOfFollowers, List<ActivityDTO> activities, boolean isLoggedUser,
                                    boolean isOnline) {

    public record ActivityDTO(String title, String description, String date, String encryptedId, ActivityType type) { }

    public enum ActivityType { FOLLOW, CREATED_POST, COMMENTED_POST, VOTED_UP,
                               VOTED_DOWN, CREATED_THREAD, COMMENTED_THREAD }
}
