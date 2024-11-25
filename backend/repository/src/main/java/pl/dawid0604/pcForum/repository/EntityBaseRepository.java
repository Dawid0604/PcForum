package pl.dawid0604.pcForum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.EntityBase;

@NoRepositoryBean
public interface EntityBaseRepository<E extends EntityBase> extends JpaRepository<E, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE #{#entityName} e SET e.encryptedId = :encryptedId WHERE e.id = :entityId")
    void updateEncryptedId(long entityId, String encryptedId);
}
