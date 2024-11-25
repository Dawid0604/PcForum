package pl.dawid0604.pcForum.dao.thread;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.dawid0604.pcForum.dao.EntityBase;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ThreadCategories")
@EqualsAndHashCode(callSuper = true)
public class ThreadCategoryEntity extends EntityBase {

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "ThumbnailPath")
    private String thumbnailPath;

    @Column(name = "CategoryLevelPathOne")
    private int categoryLevelPathOne;

    @Column(name = "CategoryLevelPathTwo")
    private Integer categoryLevelPathTwo;

    @Column(name = "CategoryLevelPathThree")
    private Integer categoryLevelPathThree;

    @SuppressWarnings("unused")
    public ThreadCategoryEntity(final String encryptedId, final String name,
                                final String description, final String thumbnailPath,
                                final int categoryLevelPathOne, final Integer categoryLevelPathTwo,
                                final Integer categoryLevelPathThree) {

        super(encryptedId);
        this.name = name;
        this.description = description;
        this.thumbnailPath = thumbnailPath;
        this.categoryLevelPathOne = categoryLevelPathOne;
        this.categoryLevelPathTwo = categoryLevelPathTwo;
        this.categoryLevelPathThree = categoryLevelPathThree;
    }
}
