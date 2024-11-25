package pl.dawid0604.pcForum.repository.thread.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;
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
