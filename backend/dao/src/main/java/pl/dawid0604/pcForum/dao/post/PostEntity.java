package pl.dawid0604.pcForum.dao.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Posts")
@EqualsAndHashCode(exclude = { "userProfile", "createdAt", "reactions", "lastUpdatedAt" }, callSuper = true)
public class PostEntity extends EntityBase {

    @ManyToOne
    @JoinColumn(name = "ThreadId")
    private ThreadEntity thread;

    @ManyToOne
    @JoinColumn(name = "UserProfileId")
    private UserProfileEntity userProfile;

    @Column(name = "Content")
    private String content;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "LastUpdatedAt")
    private LocalDateTime lastUpdatedAt;

    @Setter
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostReactionEntity> reactions;

    @SuppressWarnings("unused")
    public PostEntity(final String encryptedId, final UserProfileEntity userProfile,
                      final LocalDateTime createdAt, final LocalDateTime lastUpdatedAt) {

        super(encryptedId);
        this.userProfile = userProfile;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @SuppressWarnings("unused")
    public PostEntity(final String encryptedId, final String content, final LocalDateTime createdAt,
                      final LocalDateTime lastUpdatedAt, final UserProfileEntity userProfile) {

        super(encryptedId);
        this.userProfile = userProfile;
        this.content = content;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
