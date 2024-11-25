package pl.dawid0604.pcForum.dao.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserProfileObservations")
@EqualsAndHashCode(exclude = { "profile", "observedProfile", "observationDate" }, callSuper = true)
public class UserProfileObservationEntity extends EntityBase {

    @ManyToOne
    @JoinColumn(name = "ProfileId")
    private UserProfileEntity profile;

    @ManyToOne
    @JoinColumn(name = "ObservedProfileId")
    private UserProfileEntity observedProfile;

    @Column(name = "ObservationDate")
    private LocalDateTime observationDate;
}
