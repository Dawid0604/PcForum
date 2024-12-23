package pl.dawid0604.pcForum.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.user.ActivitySummaryDTO;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;
import pl.dawid0604.pcForum.dto.user.UsersDTO;
import pl.dawid0604.pcForum.service.user.UserProfileObservationRestService;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;
import pl.dawid0604.pcForum.service.user.UserProfileVisitorRestService;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserRestController {
    private final UserProfileRestService userProfileRestService;
    private final UserProfileVisitorRestService userProfileVisitorRestService;
    private final UserProfileObservationRestService userProfileObservationRestService;

    @GetMapping("/user/profile/base")
    public ResponseEntity<?> getUserProfileBaseInfo() {
        return new ResponseEntity<>(userProfileRestService.getUserProfileBaseInfo(), OK);
    }

    @GetMapping("/user/profile/{encryptedId}/details")
    public ResponseEntity<?> getUserProfileDetailsInfo(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        return new ResponseEntity<>(userProfileRestService.getUserProfileDetailsInfo(encryptedUserProfileId), OK);
    }

    @PostMapping("/access/register")
    public ResponseEntity<?> register(@RequestBody final UserRegistartionDTO payload) {
        return userProfileRestService.register(payload);
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/user/profile/{encryptedId}/handle/view")
    public void handleProfileView(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        userProfileVisitorRestService.handleProfileView(encryptedUserProfileId);
    }

    @ResponseStatus(NO_CONTENT)
    @PatchMapping("/user/profile/{encryptedId}/handle/observe")
    public void handleProfileFollow(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        userProfileObservationRestService.handleProfileFollow(encryptedUserProfileId);
    }

    @ResponseStatus(OK)
    @GetMapping("/user/activity/summary")
    public ActivitySummaryDTO getActivitySummary(@RequestParam(required = false, name = "sort", defaultValue = "WEEK" ) final ActivitySummarySortType summarySortType,
                                                 @RequestParam(required = false, name = "amount", defaultValue = "5") final int numberOfUsers) {

        return userProfileRestService.getActivitySummary(summarySortType, numberOfUsers);
    }

    @ResponseStatus(OK)
    @GetMapping("/users/online")
    public UsersDTO getNumberOfOnlineUsers() {
        return userProfileRestService.getNumberOfOnlineUsers();
    }

    @GetMapping("/user/profile/{encryptedId}/threads")
    public ResponseEntity<?> findUserProfileThreads(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        return new ResponseEntity<>(userProfileRestService.findUserProfileThreads(encryptedUserProfileId), OK);
    }

    @GetMapping("/user/profile/{encryptedId}/posts")
    public ResponseEntity<?> findUserProfilePosts(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        return new ResponseEntity<>(userProfileRestService.findUserProfilePosts(encryptedUserProfileId), OK);
    }

    @GetMapping("/user/profile/{encryptedId}/visitors")
    public ResponseEntity<?> findUserProfileVisitors(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        return new ResponseEntity<>(userProfileRestService.findUserProfileVisitors(encryptedUserProfileId), OK);
    }

    @GetMapping("/user/profile/{encryptedId}/observations")
    public ResponseEntity<?> findUserProfileObservations(@PathVariable("encryptedId") final String encryptedUserProfileId) {
        return new ResponseEntity<>(userProfileRestService.findUserProfileObservations(encryptedUserProfileId), OK);
    }
}
