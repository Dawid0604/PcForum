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

    @SuppressWarnings("unused")
    public UserProfileObservationEntity(final long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public UserProfileObservationEntity(final String encryptedId, final UserProfileEntity profile,
                                        final LocalDateTime observationDate) {
        super(encryptedId);
        this.profile = profile;
        this.observationDate = observationDate;
    }
}
