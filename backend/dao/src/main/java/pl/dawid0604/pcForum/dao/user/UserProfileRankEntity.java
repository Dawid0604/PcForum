package pl.dawid0604.pcForum.dao.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "UserProfileRanks")
@EqualsAndHashCode(callSuper = true)
public class UserProfileRankEntity extends EntityBase {

    @Column(name = "Name")
    private String name;

    @Column(name = "MinPoints")
    private int minPoints;

    @Column(name = "MaxPoints")
    private int maxPoints;

    @SuppressWarnings("unused")
    public UserProfileRankEntity(final String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public UserProfileRankEntity(final long id) {
        this.id = id;
    }
}
