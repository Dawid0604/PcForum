package pl.dawid0604.pcForum.service.dao.impl.browser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostEntity;
import pl.dawid0604.pcForum.dao.post.PostEntityContent;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.service.dao.browser.BrowserDaoService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static pl.dawid0604.pcForum.utils.RegexUtils.patternFrom;

@Service
@RequiredArgsConstructor
class BrowserDaoServiceImpl implements BrowserDaoService {
    private static final Pattern WORDS_PATTERN = patternFrom("\\b");

    private static final String THREADS_QUERY = """
                SELECT t.EncryptedId, t.Title, t.CreatedAt, t.LastActivity, up.EncryptedId, up.Avatar, up.Nickname,
                       t.CategoryLevelPathOne, t.CategoryLevelPathTwo, t.CategoryLevelPathThree, t.NumberOfViews
                FROM Threads t
                JOIN UserProfiles up ON t.UserProfileId = up.Id
                WHERE MATCH(t.Title, t.Content) AGAINST('%s' IN BOOLEAN MODE)
                LIMIT %d OFFSET %d
            """;

    private static final String USER_PROFILES_QUERY = """
                SELECT p.EncryptedId, p.Avatar, p.Nickname, r.Name
                FROM UserProfiles p
                JOIN UserProfileRanks r ON p.RankId = r.Id
                WHERE MATCH(p.Nickname) AGAINST('%s' IN BOOLEAN MODE)
                LIMIT %d OFFSET %d
            """;

    private static final String POSTS_QUERY = """
                SELECT p.EncryptedId, up.EncryptedId, up.Avatar, up.Nickname,
                       p.CreatedAt, t.EncryptedId, t.Title, p.Content
                FROM Posts p
                JOIN UserProfiles up ON p.UserProfileId = up.Id
                JOIN Threads t ON p.ThreadId = t.Id
                WHERE MATCH(p.Content) AGAINST('%s' IN BOOLEAN MODE)
                LIMIT %d OFFSET %d
            """;

    @PersistenceContext
    private EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ThreadEntity> findThreadsByText(final String text, int page, int size) {
        return entityManager.createNativeQuery(String.format(THREADS_QUERY, getWordsToMatch(text), size, page * size))
                            .getResultList()
                            .stream()
                            .map(_fields -> mapThread((Object[]) _fields))
                            .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PostEntity> findPostsByText(final String text, int page, int size) {
        return entityManager.createNativeQuery(String.format(POSTS_QUERY, getWordsToMatch(text), size, page * size))
                            .getResultList()
                            .stream()
                            .map(_fields -> mapPost((Object[]) _fields))
                            .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<UserProfileEntity> findUserProfilesByText(final String text, int page, int size) {
        return entityManager.createNativeQuery(String.format(USER_PROFILES_QUERY, getWordsToMatch(text), size, page * size))
                            .getResultList()
                            .stream()
                            .map(_fields -> mapUserProfile((Object[]) _fields))
                            .toList();
    }

    private static String getWordsToMatch(final String text) {
        return Arrays.stream(WORDS_PATTERN.split(text))
                     .filter(StringUtils::isNotBlank)
                     .map(_word -> "*" + _word + "*")
                     .collect(joining(" "));
    }

    private static ThreadEntity mapThread(final Object[] fields) {
        var userProfile = UserProfileEntity.builder()
                                           .encryptedId((String) fields[4])
                                           .avatar((String) fields[5])
                                           .nickname((String) fields[6])
                                           .build();

        return ThreadEntity.builder()
                           .encryptedId((String) fields[0])
                           .title((String) fields[1])
                           .createdAt(((Timestamp) fields[2]).toLocalDateTime())
                           .lastActivity(((Timestamp) fields[3]).toLocalDateTime())
                           .categoryLevelPathOne((Integer) fields[7])
                           .categoryLevelPathTwo((Integer) fields[8])
                           .categoryLevelPathThree((Integer) fields[9])
                           .numberOfViews((Integer) fields[10])
                           .userProfile(userProfile)
                           .build();
    }

    private static UserProfileEntity mapUserProfile(final Object[] fields) {
        var rank = UserProfileRankEntity.builder()
                                        .name((String) fields[3])
                                        .build();

        return UserProfileEntity.builder()
                               .encryptedId((String) fields[0])
                               .avatar((String) fields[1])
                               .nickname((String) fields[2])
                               .rank(rank)
                               .build();
    }

    private PostEntity mapPost(final Object[] fields) {
        List<PostEntityContent> content;

        try {
            content = objectMapper.readValue((String) fields[7], new TypeReference<>() { });

        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid post content", exception);
        }

        var userProfile = UserProfileEntity.builder()
                                           .encryptedId((String) fields[1])
                                           .avatar((String) fields[2])
                                           .nickname((String) fields[3])
                                           .build();

        var thread = ThreadEntity.builder()
                                 .encryptedId((String) fields[5])
                                 .title((String) fields[6])
                                 .build();

        return PostEntity.builder()
                         .encryptedId((String) fields[0])
                         .createdAt(((Timestamp) fields[4]).toLocalDateTime())
                         .thread(thread)
                         .content(content)
                         .userProfile(userProfile)
                         .build();
    }
}
