package pl.dawid0604.pcForum.dao.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserProfiles")
@EqualsAndHashCode(exclude = { "createdAt", "user", "lastActivity", "rank", "threads" }, callSuper = true)
public class UserProfileEntity extends EntityBase {

    @Setter
    @Column(name = "Avatar")
    private String avatar;

    @Setter
    @Column(name = "Background")
    private String background;

    @Setter
    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "Nickname")
    private String nickname;

    @OneToOne
    @JoinColumn(name = "UserId")
    private UserEntity user;

    @Column(name = "LastActivity")
    private LocalDateTime lastActivity;

    @Column(name = "IsOnline")
    private boolean isOnline;

    @ManyToOne
    @JoinColumn(name = "RankId")
    private UserProfileRankEntity rank;

    @Setter
    @OneToMany(mappedBy = "userProfile", orphanRemoval = true)
    private List<ThreadEntity> threads;

    @Setter
    @OneToMany(mappedBy = "userProfile", orphanRemoval = true)
    private List<PostEntity> posts;

    @SuppressWarnings("unused")
    public UserProfileEntity(final long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public UserProfileEntity(final String nickname) {
        this.nickname = nickname;
    }

    @SuppressWarnings("unused")
    public UserProfileEntity(final long id, final String encryptedId) {
        super(encryptedId);
        this.id = id;
    }

    @SuppressWarnings("unused")
    public UserProfileEntity(final String encryptedId, final String avatar, final String nickname) {
        super(encryptedId);
        this.avatar = avatar;
        this.nickname = nickname;
    }

    @SuppressWarnings("unused")
    public UserProfileEntity(final String encryptedId, final String avatar,
                             final String nickname, final UserProfileRankEntity rank) {

        super(encryptedId);
        this.avatar = avatar;
        this.nickname = nickname;
        this.rank = rank;
    }

    @SuppressWarnings("unused")
    public UserProfileEntity(final String encryptedId, final String avatar, final LocalDateTime createdAt,
                             final String nickname, final LocalDateTime lastActivity, final boolean isOnline,
                             final UserProfileRankEntity rank) {

        super(encryptedId);
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.lastActivity = lastActivity;
        this.isOnline = isOnline;
        this.rank = rank;
    }
}
