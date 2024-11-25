package pl.dawid0604.pcForum.dao.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserProfileVisitors")
@EqualsAndHashCode(exclude = { "profile", "visitor", "firstVisitDate", "lastVisitDate" }, callSuper = true)
public class UserProfileVisitorEntity extends EntityBase {

    @ManyToOne
    @JoinColumn(name = "ProfileId")
    private UserProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "VisitorProfileId")
    private UserProfileEntity visitor;

    @Setter
    @Column(name = "FirstVisitDate")
    private LocalDateTime firstVisitDate;

    @Setter
    @Column(name = "LastVisitDate")
    private LocalDateTime lastVisitDate;
}
