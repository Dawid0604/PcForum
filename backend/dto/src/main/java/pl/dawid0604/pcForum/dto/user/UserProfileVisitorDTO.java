package pl.dawid0604.pcForum.dto.user;

public record UserProfileVisitorDTO(String encryptedId, String nickname, String avatar,
                                    String firstVisitDate, String lastVisitDate) { }
