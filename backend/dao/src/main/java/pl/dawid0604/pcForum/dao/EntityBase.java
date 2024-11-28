package pl.dawid0604.pcForum.dao;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.IDENTITY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode
public abstract class EntityBase {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;

    @Column(name = "EncryptedId")
    protected String encryptedId;

    public void setEncryptedId(final String encryptedId) {
        if(isBlank(this.encryptedId)) {
            this.encryptedId = encryptedId;
        }
    }

    protected EntityBase(final String encryptedId) {
        this.encryptedId = encryptedId;
    }
}
