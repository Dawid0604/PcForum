package pl.dawid0604.pcForum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pl.dawid0604.pcForum.dao.EntityBase;

@NoRepositoryBean
public interface EntityBaseRepository<E extends EntityBase> extends JpaRepository<E, Long> { }
