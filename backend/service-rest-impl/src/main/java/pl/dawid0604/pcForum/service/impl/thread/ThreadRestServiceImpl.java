package pl.dawid0604.pcForum.service.impl.thread;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.thread.ThreadCategoryEntity;
import pl.dawid0604.pcForum.dao.thread.ThreadEntity;
import pl.dawid0604.pcForum.dto.post.ThreadNewestPostDTO;
import pl.dawid0604.pcForum.dto.thread.*;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadCategoryDaoService;
import pl.dawid0604.pcForum.service.dao.thread.ThreadDaoService;
import pl.dawid0604.pcForum.service.thread.ThreadRestService;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;

@Service
@RequiredArgsConstructor
class ThreadRestServiceImpl implements ThreadRestService {
    private final ThreadDaoService threadDaoService;
    private final ThreadCategoryDaoService threadCategoryDaoService;
    private final PostDaoService postDaoService;
    private final PostReactionDaoService postReactionDaoService;

    @Override
    @Transactional(readOnly = true)
    public List<GroupedThreadCategoryDTO> findThreadCategories() {
        var foundCategories = threadCategoryDaoService.findAll()
                                                      .stream()
                                                      .collect(groupingBy(ThreadCategoryEntity::getCategoryLevelPathOne));

        List<GroupedThreadCategoryDTO> groupedCategories = new LinkedList<>();
        for(var _groupedEntry: foundCategories.entrySet()) {
            var entryCategories = _groupedEntry.getValue()
                                               .stream()
                                               .collect(groupingBy(_category -> Pair.of(_category.getCategoryLevelPathOne(), _category.getCategoryLevelPathTwo())));

            ThreadCategoryEntity rootCategory = entryCategories.entrySet()
                                                               .stream()
                                                               .filter(_entry -> _entry.getKey().getValue() == null)
                                                               .findFirst()
                                                               .map(_entry -> _entry.getValue().get(0))
                                                               .orElseThrow();

            var entryRemainingCategories = entryCategories.entrySet()
                                                          .stream()
                                                          .filter(_entry -> _entry.getKey().getValue() != null)
                                                          .toList();

            List<ThreadCategoryDTO> subCategories = new LinkedList<>();
            for(var _entry: entryRemainingCategories) {
                var _rootCategory = _entry.getValue()
                                          .stream()
                                          .filter(_category -> _category.getCategoryLevelPathThree() == null)
                                          .findFirst()
                                          .orElseThrow();

                var _subCategories = _entry.getValue()
                                           .stream()
                                           .filter(_category -> _category.getCategoryLevelPathThree() != null)
                                           .toList();

                subCategories.add(mapCategory(_rootCategory, _subCategories));
            }

            groupedCategories.add(new GroupedThreadCategoryDTO(rootCategory.getEncryptedId(), rootCategory.getName(), subCategories,
                                                               getNewestThread(rootCategory.getCategoryLevelPathOne(), rootCategory.getCategoryLevelPathTwo(),
                                                               rootCategory.getCategoryLevelPathThree())));
        } return groupedCategories;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThreadDTO> findAllByCategoryPath(final String encryptedCategoryId, final int page, final int size) {
        var category = threadCategoryDaoService.findPathById(encryptedCategoryId)
                                               .orElseThrow();

        return threadDaoService.findAllByCategory(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                                  category.getCategoryLevelPathThree(), page, size)
                               .map(this::mapThread);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupedThreadCategoryDTO findThreadSubCategories(final String encryptedParentCategoryId) {
        var parentCategory = threadCategoryDaoService.findById(encryptedParentCategoryId)
                                                     .orElseThrow();

        var subCategories = threadCategoryDaoService.findAllByPathStartsWith(parentCategory.getCategoryLevelPathOne(), parentCategory.getCategoryLevelPathTwo(), parentCategory.getCategoryLevelPathThree());
        return new GroupedThreadCategoryDTO(parentCategory.getEncryptedId(), parentCategory.getName(), mapCategories(subCategories), null);
    }

    @Override
    public HttpStatus create(final NewThreadDTO payload) {
        return null;
    }

    @Override
    public void handleThreadView(final String encryptedThreadId) {
        threadDaoService.incrementThreadViews(encryptedThreadId);
    }

    @Override
    public List<MostPopularThreadDTO> findMostPopularThreads(final int numberOfThreads) {
        return threadDaoService.findMostPopularThreads(numberOfThreads)
                               .stream()
                               .map(this::mapMostPopularThread)
                               .toList();
    }

    private MostPopularThreadDTO mapMostPopularThread(final ThreadEntity threadEntity) {
        var user = new UserProfileDTO(threadEntity.getUserProfile().getEncryptedId(), threadEntity.getUserProfile().getNickname(),
                                      threadEntity.getUserProfile().getAvatar());

        return new MostPopularThreadDTO(threadEntity.getEncryptedId(), threadEntity.getTitle(), user, formatDate(threadEntity.getCreatedAt()),
                                        postDaoService.countPostsByThread(threadEntity.getEncryptedId()));
    }

    @Override
    public ThreadDetailsDTO findThreadDetails(final String encryptedThreadId) {
        return threadDaoService.findDetailsById(encryptedThreadId)
                               .map(this::mapThreadDetails)
                               .orElseThrow();
    }

    @Transactional(readOnly = true)
    private ThreadDetailsDTO mapThreadDetails(final ThreadEntity threadEntity) {
        UserProfileDTO threadAuthor = new UserProfileDTO(threadEntity.getUserProfile().getEncryptedId(), threadEntity.getUserProfile().getNickname(),
                                                         threadEntity.getUserProfile().getAvatar(), threadEntity.getUserProfile().getRank().getName(),
                                                         postDaoService.countPostsByUser(threadEntity.getUserProfile().getEncryptedId()),
                                                         postReactionDaoService.countUpVotesByUser(threadEntity.getUserProfile().getEncryptedId()),
                                                         postReactionDaoService.countDownVotesByUser(threadEntity.getUserProfile().getEncryptedId()));

        String subCategoryName = null;
        String categoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), null, null)
                                                      .orElseThrow();

        if(threadEntity.getCategoryLevelPathTwo() != null) {
            subCategoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), threadEntity.getCategoryLevelPathTwo(), threadEntity.getCategoryLevelPathThree())
                                                      .orElseThrow();
        }

        return new ThreadDetailsDTO(threadEntity.getEncryptedId(), threadEntity.getTitle(), threadEntity.getContent(), formatDate(threadEntity.getCreatedAt()),
                                    formatDate(threadEntity.getLastUpdatedAt()), formatDate(threadEntity.getLastActivity()),
                                    threadEntity.isClosed(), threadAuthor, categoryName, subCategoryName);
    }

    @Transactional(readOnly = true)
    private ThreadDTO mapThread(final ThreadEntity threadEntity) {
        String categoryName = null;

        if(threadEntity.getCategoryLevelPathTwo() != null && threadEntity.getCategoryLevelPathThree() != null) {
            categoryName = threadCategoryDaoService.findCategoryName(threadEntity.getCategoryLevelPathOne(), threadEntity.getCategoryLevelPathTwo(),
                                                                     threadEntity.getCategoryLevelPathThree())
                                                   .orElseThrow();
        }

        return new ThreadDTO(threadEntity.getEncryptedId(), threadEntity.getTitle(), threadEntity.getUserProfile().getEncryptedId(),
                             threadEntity.getUserProfile().getNickname(), threadEntity.getUserProfile().getAvatar(),
                             formatDate(threadEntity.getLastActivity()), categoryName, getNewestPost(threadEntity.getEncryptedId()),
                             threadEntity.getNumberOfViews(), postDaoService.countPostsByThread(threadEntity.getEncryptedId()));
    }

    private List<ThreadCategoryDTO> mapCategories(final List<ThreadCategoryEntity> categories) {
        return categories.stream()
                         .map(this::mapCategory)
                         .toList();
    }

    @Transactional(readOnly = true)
    private ThreadCategoryDTO mapCategory(final ThreadCategoryEntity category) {
        long numberOfPosts = postDaoService.countPostsByCategory(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                                                 category.getCategoryLevelPathThree());

        long numberOfThreads = threadDaoService.countByCategory(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                                                category.getCategoryLevelPathThree());

        var newestThread = getNewestThread(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                           category.getCategoryLevelPathThree());

        return new ThreadCategoryDTO(category.getEncryptedId(), category.getName(), category.getDescription(), numberOfThreads, numberOfPosts, emptyList(), newestThread);
    }

    @Transactional(readOnly = true)
    private ThreadCategoryDTO mapCategory(final ThreadCategoryEntity category, final List<ThreadCategoryEntity> subCategories) {
        long numberOfPosts = postDaoService.countPostsByCategory(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                                                 category.getCategoryLevelPathThree());

        long numberOfThreads = threadDaoService.countByCategory(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo());

        var newestThread = getNewestThread(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo(),
                                           category.getCategoryLevelPathThree());

        var mappedSubCategories = subCategories.stream()
                                               .map(this::mapCategory)
                                               .toList();

        return new ThreadCategoryDTO(category.getEncryptedId(), category.getName(), category.getDescription(),
                                     numberOfThreads, numberOfPosts, mappedSubCategories, newestThread);
    }

    @Transactional(readOnly = true)
    private ThreadDTO getNewestThread(final int categoryLevelPathOne, final Integer categoryLevelPathTwo, final Integer categoryLevelPathThree) {
        return threadDaoService.findNewestByCategory(categoryLevelPathOne, categoryLevelPathTwo, categoryLevelPathThree)
                               .map(this::mapThread)
                               .orElse(null);
    }

    @Transactional(readOnly = true)
    private ThreadNewestPostDTO getNewestPost(final String encryptedThreadId) {
        return postDaoService.findNewestByThreadId(encryptedThreadId)
                             .map(_post -> new ThreadNewestPostDTO(_post.getEncryptedId(), _post.getUserProfile().getEncryptedId(), _post.getUserProfile().getNickname(),
                                                                   _post.getUserProfile().getAvatar(), formatDate(_post.getCreatedAt())))
                             .orElse(null);
    }
}
