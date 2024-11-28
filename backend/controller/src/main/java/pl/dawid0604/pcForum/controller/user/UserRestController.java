package pl.dawid0604.pcForum.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class UserRestController {
    private final UserProfileRestService userProfileRestService;

    @GetMapping("/user/profile/base")
    public ResponseEntity<?> getUserProfileBaseInfo() {
        return new ResponseEntity<>(userProfileRestService.getUserProfileBaseInfo(), OK);
    }

    @PostMapping("/access/register")
    public ResponseEntity<?> register(@RequestBody final UserRegistartionDTO payload) {
        return userProfileRestService.register(payload);
    }
}
