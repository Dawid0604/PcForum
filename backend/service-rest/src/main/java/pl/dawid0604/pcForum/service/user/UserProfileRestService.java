package pl.dawid0604.pcForum.service.user;

import org.springframework.http.ResponseEntity;
import pl.dawid0604.pcForum.dto.user.ActivitySummaryDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.dto.user.UserProfileDetailsDTO;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

public interface UserProfileRestService {
    UserProfileDTO getUserProfileBaseInfo();

    ResponseEntity<?> register(UserRegistartionDTO payload);

    UserProfileDetailsDTO getUserProfileDetailsInfo(String encryptedUserProfileId);

    ActivitySummaryDTO getActivitySummary(ActivitySummarySortType summarySortType, int numberOfUsers);
}
