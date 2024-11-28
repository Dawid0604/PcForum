package pl.dawid0604.pcForum.service.impl.thread;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;
import pl.dawid0604.pcForum.service.thread.ThreadRestService;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static pl.dawid0604.pcForum.utils.DateFormatter.formatDate;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
@RequiredArgsConstructor
class ThreadRestServiceImpl implements ThreadRestService {
    private final ThreadDaoService threadDaoService;
    private final ThreadCategoryDaoService threadCategoryDaoService;
    private final PostDaoService postDaoService;
    private final PostReactionDaoService postReactionDaoService;
    private final UserProfileDaoService userProfileDaoService;

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
    public List<CreatorThreadCategoryDTO> findCreatorThreadCategories() {
        List<CreatorThreadCategoryDTO> groupedCategories = new LinkedList<>();
        var categories = threadCategoryDaoService.findAllCreatorCategories()
                                                 .stream()
                                                 .collect(groupingBy(ThreadCategoryEntity::getCategoryLevelPathOne));

        for(var _groupedEntry: categories.entrySet()) {
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

            List<CreatorThreadCategoryDTO.ThreadSubCategory> subCategories = new LinkedList<>();
            for(var _entry: entryRemainingCategories) {
                var category = _entry.getValue()
                                     .stream()
                                     .filter(_category -> _category.getCategoryLevelPathThree() == null)
                                     .findFirst()
                                     .orElseThrow();

                subCategories.add(new CreatorThreadCategoryDTO.ThreadSubCategory(category.getEncryptedId(), category.getName()));
            }

            groupedCategories.add(new CreatorThreadCategoryDTO(rootCategory.getName(), subCategories));

        } return groupedCategories;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CreatorThreadCategoryDTO.ThreadSubCategory> findCreatorThreadCategorySubCategories(final String encryptedCategoryId) {
        var category = threadCategoryDaoService.findCategoryPathById(encryptedCategoryId)
                                               .orElseThrow();

        return threadCategoryDaoService.findAllCreatorCategorySubCategories(category.getCategoryLevelPathOne(), category.getCategoryLevelPathTwo())
                                       .stream()
                                       .map(_threadCategory -> new CreatorThreadCategoryDTO.ThreadSubCategory(_threadCategory.getEncryptedId(), _threadCategory.getName()))
                                       .toList();
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
    @Transactional
    public NewThreadResponseDTO create(final NewThreadDTO payload) {
        if(isBlank(payload.title())) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        if(isBlank(payload.content())) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        if(isBlank(payload.encryptedCategoryId())) {
            throw new IllegalArgumentException("Category not specified");
        }

        var loggedUserUsername = ((User) SecurityContextHolder.getContext().getAuthentication()
                                                                           .getPrincipal())
                                                                           .getUsername();

        var loggedUserProfile = userProfileDaoService.findByUsername(loggedUserUsername)
                                                     .orElseThrow();

        var category = threadCategoryDaoService.findCategoryPathById(payload.encryptedCategoryId())
                                               .orElseThrow();

        var thread = ThreadEntity.builder()
                                 .userProfile(loggedUserProfile)
                                 .createdAt(getCurrentDate())
                                 .isClosed(false)
                                 .content(payload.content())
                                 .title(payload.title())
                                 .categoryLevelPathOne(category.getCategoryLevelPathOne())
                                 .categoryLevelPathTwo(category.getCategoryLevelPathTwo())
                                 .categoryLevelPathThree(category.getCategoryLevelPathThree())
                                 .build();

        thread = threadDaoService.save(thread);
        return new NewThreadResponseDTO(thread.getEncryptedId(), thread.getTitle());
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

        return new ThreadCategoryDTO(category.getEncryptedId(), category.getName(), category.getDescription(), numberOfThreads,
                                     numberOfPosts, category.getThumbnailPath(), emptyList(), newestThread);
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
                                     numberOfThreads, numberOfPosts, category.getThumbnailPath(), mappedSubCategories, newestThread);
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
