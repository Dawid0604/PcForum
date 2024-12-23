package pl.dawid0604.pcForum.dto.user;

public record UserProfileObservationDTO(String encryptedId, String nickname, String avatar,
                                        String observationDateFrom) { }
