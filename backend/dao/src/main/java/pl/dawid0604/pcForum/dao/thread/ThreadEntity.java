package pl.dawid0604.pcForum.dao.thread;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Threads")
@EqualsAndHashCode(exclude = { "userProfile", "createdAt",
                               "lastUpdatedAt", "lastActivity" }, callSuper = true)
public class ThreadEntity extends EntityBase {

    @Column(name = "Title")
    private String title;

    @Column(name = "Content")
    private String content;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "LastUpdatedAt")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "LastActivity")
    private LocalDateTime lastActivity;

    @Column(name = "IsClosed")
    private boolean isClosed;

    @Column(name = "CategoryLevelPathOne")
    private int categoryLevelPathOne;

    @Column(name = "CategoryLevelPathTwo")
    private Integer categoryLevelPathTwo;

    @Column(name = "CategoryLevelPathThree")
    private Integer categoryLevelPathThree;

    @OneToMany(mappedBy = "thread", orphanRemoval = true)
    private List<PostEntity> posts;

    @ManyToOne
    @JoinColumn(name = "UserProfileId")
    private UserProfileEntity userProfile;

    @SuppressWarnings("unused")
    public ThreadEntity(final String encryptedId, final String title, final LocalDateTime createdAt,
                        final LocalDateTime lastActivity, final UserProfileEntity userProfile,
                        final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                        final Integer categoryLevelPathThree) {

        super(encryptedId);
        this.title = title;
        this.createdAt = createdAt;
        this.lastActivity = lastActivity;
        this.userProfile = userProfile;
        this.categoryLevelPathOne = categoryLevelPathOne;
        this.categoryLevelPathTwo = categoryLevelPathTwo;
        this.categoryLevelPathThree = categoryLevelPathThree;
    }

    @SuppressWarnings("unused")
    public ThreadEntity(final String encryptedId, final String title, final String content, final LocalDateTime createdAt,
                        final LocalDateTime lastUpdatedAt, final LocalDateTime lastActivity, final boolean isClosed,
                        final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                        final Integer categoryLevelPathThree, final UserProfileEntity userProfile) {

        super(encryptedId);
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastActivity = lastActivity;
        this.isClosed = isClosed;
        this.categoryLevelPathOne = categoryLevelPathOne;
        this.categoryLevelPathTwo = categoryLevelPathTwo;
        this.categoryLevelPathThree = categoryLevelPathThree;
        this.userProfile = userProfile;
    }
}
