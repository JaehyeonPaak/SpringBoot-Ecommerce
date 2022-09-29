package com.floyd.common.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 120, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    @Column(name = "all_parent_ids", length = 256)
    private String allParentIDs;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<Category> children = new HashSet<>();

    public Category(String name, String alias, String image) {
        this.name = name;
        this.alias = alias;
        this.image = image;
    }

    public Category(String name, String alias, String image, Category parent) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.parent = parent;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    @Transient
    public static Category copyCategory(Category category) {
        Category copyCategory = new Category();
        copyCategory.setId(category.getId());
        copyCategory.setName(category.getName());
        copyCategory.setAlias(category.getAlias());
        copyCategory.setImage(category.getImage());
        copyCategory.setEnabled(category.isEnabled());
        copyCategory.setChildren(category.getChildren());

        return copyCategory;
    }

    public static Category copyCategory(Category subCategory, String name) {
        var copyCategory = copyCategory(subCategory);
        copyCategory.setName(name);

        return copyCategory;
    }

    @Transient
    public String getImagePath() {
        if(this.id == null || this.image == null) {
            return "/images/image-thumbnail.png";
        }
        return "/category-images/" + this.id + "/" + this.image;
    }
}
