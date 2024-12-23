package pl.dawid0604.pcForum.dto.user;

import java.util.List;

public record UserProfileObservationsDTO(String nickname, String avatar, String encryptedId,
                                         List<UserProfileObservationDTO> observations) { }
