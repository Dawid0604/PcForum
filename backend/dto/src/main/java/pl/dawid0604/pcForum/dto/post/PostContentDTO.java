package pl.dawid0604.pcForum.dto.post;

public record PostContentDTO(String content, BlockquoteMetaDTO meta) {
    public record BlockquoteMetaDTO(String authorNickname, String dateAdded) { }
}
