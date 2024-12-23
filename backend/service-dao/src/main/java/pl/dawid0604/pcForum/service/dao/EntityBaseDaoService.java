package pl.dawid0604.pcForum.service.dao;

import pl.dawid0604.pcForum.dao.EntityBase;

public interface EntityBaseDaoService<B extends EntityBase> {
    void delete(long entityId);
}
