package pl.dawid0604.pcForum.service.dao.impl;

import lombok.RequiredArgsConstructor;
import pl.dawid0604.pcForum.dao.EntityBase;
import pl.dawid0604.pcForum.repository.EntityBaseRepository;
import pl.dawid0604.pcForum.service.dao.EntityBaseDaoService;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RequiredArgsConstructor
public abstract class EntityBaseDaoServiceImpl<E extends EntityBase> implements EntityBaseDaoService<E> {
    private final EntityBaseRepository<E> repository;

    @Override
    public void updateEncryptedId(final long entityId, final String encryptedId) {
        if(isBlank(encryptedId)) {
            throw new IllegalArgumentException("Encrypted id cannot be empty");
        }

        repository.updateEncryptedId(entityId, encryptedId);
    }

    @Override
    public void delete(final long entityId) {
        repository.deleteById(entityId);
    }

    @Override
    public E save(final E entity) {
        return repository.save(entity);
    }
}
