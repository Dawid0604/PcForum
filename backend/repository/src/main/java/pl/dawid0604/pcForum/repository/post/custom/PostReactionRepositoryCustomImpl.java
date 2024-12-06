package pl.dawid0604.pcForum.repository.post.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("unused")
public class PostReactionRepositoryCustomImpl implements PostReactionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileEntity> findUsersWithMostNumberOfUpVotes(final LocalDateTime timeFrom, final int numberOfUsers) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostReactionEntity> postReactionEntityCriteriaQuery = criteriaBuilder.createQuery(PostReactionEntity.class);
        Root<PostReactionEntity> postReactionEntityRootQuery = postReactionEntityCriteriaQuery.from(PostReactionEntity.class);

        postReactionEntityCriteriaQuery.multiselect(criteriaBuilder.construct(UserProfileEntity.class, postReactionEntityRootQuery.get("post").get("userProfile").get("encryptedId"),
                                                    postReactionEntityRootQuery.get("post").get("userProfile").get("avatar"), postReactionEntityRootQuery.get("post").get("userProfile").get("nickname")));

        postReactionEntityCriteriaQuery.where(criteriaBuilder.equal(postReactionEntityRootQuery.get("upVote"), true));
        postReactionEntityCriteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(postReactionEntityRootQuery.get("createdAt"), timeFrom));
        postReactionEntityCriteriaQuery.groupBy(postReactionEntityRootQuery.get("post").get("userProfile").get("encryptedId"));
        postReactionEntityCriteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(postReactionEntityRootQuery.get("upVote"))),
                                                criteriaBuilder.asc(postReactionEntityRootQuery.get("post").get("userProfile").get("nickname")));

        return entityManager.createQuery(postReactionEntityCriteriaQuery)
                            .setMaxResults(numberOfUsers)
                            .getResultList()
                            .stream()
                            .map(PostReactionEntity::getUserProfile)
                            .toList();
    }
}
