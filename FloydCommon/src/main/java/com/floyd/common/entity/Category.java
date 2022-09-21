package com.floyd.common.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 120, nullable = false, unique = true)
    @NonNull
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    @NonNull
    private String alias;

    @Column(length = 128, nullable = false)
    @NonNull
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category(@NonNull String name, @NonNull String alias, @NonNull String image, Category parent) {
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private Set<Category> children = new HashSet<>();
}
