package pl.dawid0604.pcForum.service.user;

import org.springframework.http.ResponseEntity;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;

public interface UserProfileRestService {
    UserProfileDTO getUserProfileBaseInfo();

    ResponseEntity<?> register(UserRegistartionDTO payload);
}
