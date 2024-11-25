package pl.dawid0604.pcForum.dao.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserEntityRole {
    USER("USER_ROLE"), MODERATOR("MODERATOR"), ADMIN("ADMIN");
    private final String name;

    public String get() {
        return this.name;
    }
}
