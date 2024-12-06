package pl.dawid0604.pcForum.service.impl.post;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.dao.post.PostReactionEntity;
import pl.dawid0604.pcForum.service.dao.post.PostDaoService;
import pl.dawid0604.pcForum.service.dao.post.PostReactionDaoService;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;
import pl.dawid0604.pcForum.service.post.PostReactionRestService;

import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
@RequiredArgsConstructor
class PostReactionRestServiceImpl implements PostReactionRestService {
    private final PostReactionDaoService postReactionDaoService;
    private final UserProfileDaoService userProfileDaoService;
    private final PostDaoService postDaoService;

    @Override
    public void handleUpVote(final String postEncryptedId) {
        String loggedUserEncryptedId = getLoggedUserEncryptedId();
        var possibleReaction = postReactionDaoService.findByPostAndUser(postEncryptedId, loggedUserEncryptedId);

        if(possibleReaction.isPresent()) {
            var reaction = possibleReaction.get();
                reaction.setUpVote(!reaction.isUpVote());
                reaction.setDownVote(false);

            if(reaction.isEmpty()) {
                postReactionDaoService.deleteById(reaction.getEncryptedId());

            } else {
                postReactionDaoService.updateStatuses(postEncryptedId, reaction.isUpVote(), reaction.isDownVote());
            }

        } else {
            var post = postDaoService.findByIdWithoutFields(postEncryptedId)
                                     .orElseThrow();

            var userProfile = userProfileDaoService.findByIdWithoutFields(loggedUserEncryptedId)
                                                   .orElseThrow();

            var newReaction = PostReactionEntity.builder()
                                                .post(post)
                                                .userProfile(userProfile)
                                                .createdAt(getCurrentDate())
                                                .upVote(true)
                                                .build();

            postReactionDaoService.save(newReaction);
        }
    }

    @Override
    public void handleDownVote(final String postEncryptedId) {
        String loggedUserEncryptedId = getLoggedUserEncryptedId();
        var possibleReaction = postReactionDaoService.findByPostAndUser(postEncryptedId, loggedUserEncryptedId);

        if(possibleReaction.isPresent()) {
            var reaction = possibleReaction.get();
                reaction.setDownVote(!reaction.isDownVote());
                reaction.setUpVote(false);

            if(reaction.isEmpty()) {
                postReactionDaoService.deleteById(reaction.getEncryptedId());

            } else {
                postReactionDaoService.updateStatuses(postEncryptedId, reaction.isUpVote(), reaction.isDownVote());
            }

        } else {
            var post = postDaoService.findByIdWithoutFields(postEncryptedId)
                                     .orElseThrow();

            var userProfile = userProfileDaoService.findByIdWithoutFields(loggedUserEncryptedId)
                                                   .orElseThrow();

            var newReaction = PostReactionEntity.builder()
                                                .post(post)
                                                .userProfile(userProfile)
                                                .createdAt(getCurrentDate())
                                                .downVote(true)
                                                .build();

            postReactionDaoService.save(newReaction);
        }
    }

    private String getLoggedUserEncryptedId() {
        var loggedUserUsername = ((User) SecurityContextHolder.getContext().getAuthentication()
                                                                           .getPrincipal())
                                                                           .getUsername();

        return userProfileDaoService.findEncryptedIdByUsername(loggedUserUsername)
                                    .orElseThrow();
    }
}
