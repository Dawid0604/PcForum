package pl.dawid0604.pcForum.dto.thread;

import java.util.List;

public record CreatorThreadCategoryDTO(String name, List<ThreadSubCategory> subCategories) {
    public record ThreadSubCategory(String encryptedId, String name) { }
}
