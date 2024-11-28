package pl.dawid0604.pcForum.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dawid0604.pcForum.dao.user.UserEntity;
import pl.dawid0604.pcForum.dao.user.UserEntityRole;
import pl.dawid0604.pcForum.dao.user.UserProfileEntity;
import pl.dawid0604.pcForum.dao.user.UserProfileRankEntity;
import pl.dawid0604.pcForum.dto.user.UserProfileDTO;
import pl.dawid0604.pcForum.dto.user.UserRegistartionDTO;
import pl.dawid0604.pcForum.service.dao.encryption.EncryptionService;
import pl.dawid0604.pcForum.service.dao.user.UserDaoService;
import pl.dawid0604.pcForum.service.dao.user.UserProfileDaoService;
import pl.dawid0604.pcForum.service.dao.user.UserProfileRankDaoService;
import pl.dawid0604.pcForum.service.user.UserProfileRestService;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static pl.dawid0604.pcForum.utils.DateFormatter.getCurrentDate;

@Service
@RequiredArgsConstructor
class UserProfileRestServiceImpl implements UserProfileRestService {
    private final UserProfileDaoService userProfileDaoService;
    private final UserProfileRankDaoService userProfileRankDaoService;
    private final UserDaoService userDaoService;
    private final PasswordEncoder passwordEncoder;
    private final EncryptionService encryptionService;

    @Override
    public UserProfileDTO getUserProfileBaseInfo() {
        return userProfileDaoService.findBaseInfo(getLoggedUserUsername())
                                    .map(_user -> new UserProfileDTO(_user.getEncryptedId(), _user.getNickname(), _user.getAvatar()))
                                    .orElseThrow();
    }

    @Override
    @Transactional
    public ResponseEntity<?> register(final UserRegistartionDTO payload) {
        if(isBlank(payload.username())) {
            return new ResponseEntity<>("Username cannot be empty", BAD_REQUEST);
        }

        if(isBlank(payload.password())) {
            return new ResponseEntity<>("Password cannot be empty", BAD_REQUEST);
        }

        if(isBlank(payload.nickname())) {
            return new ResponseEntity<>("Nickname cannot be empty", BAD_REQUEST);
        }

        if(userDaoService.existsByUsername(payload.username())) {
            return new ResponseEntity<>("Username already exists", BAD_REQUEST);
        }

        if(userProfileDaoService.existsByNickname(payload.nickname())) {
            return new ResponseEntity<>("Nickname already exists", BAD_REQUEST);
        }

        UserEntity user = UserEntity.builder()
                                    .username(payload.username())
                                    .password(passwordEncoder.encode(payload.password()))
                                    .role(UserEntityRole.USER)
                                    .build();

        user = userDaoService.save(user);
        user.setEncryptedId(encryptionService.encryptUser(user.getId()));

        UserProfileRankEntity defaultRank = userProfileRankDaoService.findDefaultRank()
                                                                     .orElseThrow();

        UserProfileEntity userProfileEntity = UserProfileEntity.builder()
                                                               .nickname(payload.nickname())
                                                               .createdAt(getCurrentDate())
                                                               .user(userDaoService.save(user))
                                                               .rank(defaultRank)
                                                               .build();

        userProfileEntity = userProfileDaoService.save(userProfileEntity);
        userProfileEntity.setEncryptedId(encryptionService.encryptUserProfile(userProfileEntity.getId()));
        userProfileDaoService.save(userProfileEntity);

        return new ResponseEntity<>(CREATED);
    }

    private static String getLoggedUserUsername() {
        return ((User) SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getPrincipal()).getUsername();
    }
}
