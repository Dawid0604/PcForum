package pl.dawid0604.pcForum.service.user;

import org.springframework.http.ResponseEntity;
import pl.dawid0604.pcForum.dto.user.*;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

public interface UserProfileRestService {
    UserProfileDTO getUserProfileBaseInfo();

    ResponseEntity<?> register(UserRegistartionDTO payload);

    UserProfileDetailsDTO getUserProfileDetailsInfo(String encryptedUserProfileId);

    ActivitySummaryDTO getActivitySummary(ActivitySummarySortType summarySortType, int numberOfUsers);

    UsersDTO getNumberOfOnlineUsers();
}
