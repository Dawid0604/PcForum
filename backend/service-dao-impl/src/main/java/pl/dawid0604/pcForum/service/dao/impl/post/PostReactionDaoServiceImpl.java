package pl.dawid0604.pcForum.service.dao.impl.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.repository.post.PostReactionRepository;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.impl.EntityBaseDaoServiceImpl;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
class PostReactionDaoServiceImpl extends EntityBaseDaoServiceImpl<PostReactionEntity>
                                 implements PostReactionDaoService {

    private final PostReactionRepository postReactionRepository;
    private final EncryptionService encryptionService;

    public PostReactionDaoServiceImpl(final PostReactionRepository repository, final EncryptionService encryptionService) {
        super(repository);
        this.postReactionRepository = repository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public long countUpVotesByUser(final String userEncryptedId) {
        return postReactionRepository.countUpVotesByUser(encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countDownVotesByUser(final String userEncryptedId) {
        return postReactionRepository.countDownVotesByUser(encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countUpVotesById(final String postEncryptedId) {
        return postReactionRepository.countUpVotesById(encryptionService.decryptId(postEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public long countDownVotesById(final String postEncryptedId) {
        return postReactionRepository.countDownVotesById(encryptionService.decryptId(postEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasUpVote(final String postEncryptedId, final String userEncryptedId) {
        return postReactionRepository.userHasUpVote(encryptionService.decryptId(postEncryptedId), encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasDownVote(final String postEncryptedId, final String userEncryptedId) {
        return postReactionRepository.userHasDownVote(encryptionService.decryptId(postEncryptedId), encryptionService.decryptId(userEncryptedId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostReactionEntity> findByPostAndUser(final String postEncryptedId, final String loggedUserEncryptedId) {
        return postReactionRepository.findByPostAndUser(encryptionService.decryptId(postEncryptedId), encryptionService.decryptId(loggedUserEncryptedId));
    }

    @Override
    @Transactional
    public PostReactionEntity save(PostReactionEntity entity) {
        if(isNotBlank(entity.getEncryptedId())) {
            return postReactionRepository.save(entity);
        }

        entity = postReactionRepository.save(entity);
        entity.setEncryptedId(encryptionService.encryptPostReaction(entity.getId()));
        return postReactionRepository.save(entity);
    }

    @Override
    public void deleteById(final String encryptedId) {
        postReactionRepository.deleteReactionById(encryptionService.decryptId(encryptedId));
    }

    @Override
    public void updateStatuses(final String encryptedId, final boolean upVote, final boolean downVote) {
        postReactionRepository.updateStatuses(encryptionService.decryptId(encryptedId), upVote, downVote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileEntity> findUsersWithMostNumberOfUpVotes(final LocalDateTime timeFrom, final int numberOfUsers) {
        return postReactionRepository.findUsersWithMostNumberOfUpVotes(timeFrom, numberOfUsers);
    }

    @Override
    public long countUpVotesByIdWithTimeInterval(final String encryptedId, final LocalDateTime dateFrom) {
        return postReactionRepository.countUpVotesByIdWithTimeInterval(encryptionService.decryptId(encryptedId), dateFrom);
    }
}
