package pl.dawid0604.pcForum.repository.post.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<PostEntity> findNewestByThreadId(final long threadId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostEntity> criteriaQuery = criteriaBuilder.createQuery(PostEntity.class);
        Root<PostEntity> postEntityRootQuery = criteriaQuery.from(PostEntity.class);

        criteriaQuery.multiselect(postEntityRootQuery.get("encryptedId"), criteriaBuilder.construct(UserProfileEntity.class,
                                  postEntityRootQuery.get("userProfile").get("encryptedId"), postEntityRootQuery.get("userProfile").get("avatar"),
                                  postEntityRootQuery.get("userProfile").get("nickname")), postEntityRootQuery.get("createdAt"),
                                  postEntityRootQuery.get("lastUpdatedAt"));

        criteriaQuery.where(criteriaBuilder.equal(postEntityRootQuery.get("thread").get("id"), threadId));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.literal(5)));

        return entityManager.createQuery(criteriaQuery)
                            .setMaxResults(1)
                            .getResultList()
                            .stream()
                            .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostEntity> findNewestPosts(final int numberOfPosts) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostEntity> criteriaQuery = criteriaBuilder.createQuery(PostEntity.class);
        Root<PostEntity> postEntityRootQuery = criteriaQuery.from(PostEntity.class);

        criteriaQuery.multiselect(postEntityRootQuery.get("encryptedId"), criteriaBuilder.construct(ThreadEntity.class,
                                  postEntityRootQuery.get("thread").get("encryptedId"), postEntityRootQuery.get("thread").get("title")),
                                  criteriaBuilder.construct(UserProfileEntity.class, postEntityRootQuery.get("userProfile").get("encryptedId"),
                                  postEntityRootQuery.get("userProfile").get("avatar"), postEntityRootQuery.get("userProfile").get("nickname")),
                                  criteriaBuilder.max(postEntityRootQuery.get("createdAt")));

        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.literal(7)));
        criteriaQuery.groupBy(postEntityRootQuery.get("thread").get("encryptedId"), postEntityRootQuery.get("userProfile").get("encryptedId"));

        return entityManager.createQuery(criteriaQuery)
                            .setMaxResults(numberOfPosts)
                            .getResultList();
    }
}
