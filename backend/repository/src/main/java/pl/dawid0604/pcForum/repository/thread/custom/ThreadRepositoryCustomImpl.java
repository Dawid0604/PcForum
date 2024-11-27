package pl.dawid0604.pcForum.repository.thread.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ThreadRepositoryCustomImpl implements ThreadRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<ThreadEntity> findNewestByCategory(final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                                       final Integer categoryLevelPathThree) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ThreadEntity> criteriaQuery = criteriaBuilder.createQuery(ThreadEntity.class);
        Root<ThreadEntity> rootThreadQuery = criteriaQuery.from(ThreadEntity.class);

        criteriaQuery.multiselect(rootThreadQuery.get("encryptedId"), rootThreadQuery.get("title"), rootThreadQuery.get("createdAt"),
                                  rootThreadQuery.get("lastActivity"), criteriaBuilder.construct(UserProfileEntity.class,
                                  rootThreadQuery.get("userProfile").get("encryptedId"), rootThreadQuery.get("userProfile").get("avatar"),
                                  rootThreadQuery.get("userProfile").get("nickname")), rootThreadQuery.get("categoryLevelPathOne"),
                                  rootThreadQuery.get("categoryLevelPathTwo"), rootThreadQuery.get("categoryLevelPathThree"));

        criteriaQuery.where(getWhereExpression(criteriaBuilder, rootThreadQuery, categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree))
                     .orderBy(criteriaBuilder.desc(criteriaBuilder.literal(3)));

        return entityManager.createQuery(criteriaQuery)
                            .setMaxResults(1)
                            .getResultList()
                            .stream()
                            .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadEntity> findMostPopularThreads(final int numberOfThreads) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostEntity> criteriaQuery = criteriaBuilder.createQuery(PostEntity.class);
        Root<PostEntity> postEntityRootQuery = criteriaQuery.from(PostEntity.class);

        criteriaQuery.multiselect(criteriaBuilder.construct(ThreadEntity.class, postEntityRootQuery.get("thread").get("encryptedId"),
                                  postEntityRootQuery.get("thread").get("title"), postEntityRootQuery.get("thread").get("createdAt"),
                                  criteriaBuilder.construct(UserProfileEntity.class, postEntityRootQuery.get("thread").get("userProfile").get("encryptedId"),
                                  postEntityRootQuery.get("thread").get("userProfile").get("avatar"), postEntityRootQuery.get("thread").get("userProfile").get("nickname"))));

        criteriaQuery.groupBy(postEntityRootQuery.get("thread").get("id"));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(postEntityRootQuery)));

        return entityManager.createQuery(criteriaQuery)
                            .setMaxResults(numberOfThreads)
                            .getResultList()
                            .stream()
                            .map(PostEntity::getThread)
                            .toList();
    }

    private static Expression<Boolean> getWhereExpression(final CriteriaBuilder criteriaBuilder, final Root<ThreadEntity> rootThreadQuery,
                                                          final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                                          final Integer categoryLevelPathThree) {

        List<Predicate> predicates = new ArrayList<>(3);
                        predicates.add(criteriaBuilder.equal(rootThreadQuery.get("categoryLevelPathOne"), categoryLevelPathOne));

        if(categoryLevelPathTwo != null) {
            predicates.add(criteriaBuilder.equal(rootThreadQuery.get("categoryLevelPathTwo"), categoryLevelPathTwo));
        }

        if(categoryLevelPathThree != null) {
            predicates.add(criteriaBuilder.equal(rootThreadQuery.get("categoryLevelPathThree"), categoryLevelPathThree));
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
