package pl.dawid0604.pcForum.service.dao.impl;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.service.dao.EntityBaseDaoService;

@RequiredArgsConstructor
public abstract class EntityBaseDaoServiceImpl<E extends EntityBase> implements EntityBaseDaoService<E> {
    private final EntityBaseRepository<E> repository;

    @Override
    public void delete(final long entityId) {
        repository.deleteById(entityId);
    }

    public abstract E save(E entity);
}
