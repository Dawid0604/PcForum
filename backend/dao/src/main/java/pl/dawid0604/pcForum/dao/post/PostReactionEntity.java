package pl.dawid0604.pcForum.dao.post;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PostReactions")
@EqualsAndHashCode(exclude = { "userProfile", "createdAt", "post" }, callSuper = true)
public class PostReactionEntity extends EntityBase {

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "UserProfileId")
    private UserProfileEntity userProfile;

    @ManyToOne
    @JoinColumn(name = "PostId")
    private PostEntity post;

    @Setter
    @Column(name = "UpVote")
    private boolean upVote;

    @Setter
    @Column(name = "DownVote")
    private boolean downVote;

    @SuppressWarnings("unused")
    public PostReactionEntity(final UserProfileEntity userProfile) {
        this.userProfile = userProfile;
    }

    @SuppressWarnings("unused")
    public PostReactionEntity(final String encryptedId, final boolean upVote,
                              final boolean downVote) {

        super(encryptedId);
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public boolean isEmpty() {
        return !upVote && !downVote;
    }
}
