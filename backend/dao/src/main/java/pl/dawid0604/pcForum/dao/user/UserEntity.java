package pl.dawid0604.pcForum.dao.user;

import jakarta.persistence.*;
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
@Table(name = "Users")
@EqualsAndHashCode(exclude = { "profile" }, callSuper = true)
public class UserEntity extends EntityBase {

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private UserEntityRole role;

    @OneToOne(mappedBy = "user", orphanRemoval = true)
    private UserProfileEntity profile;
}
