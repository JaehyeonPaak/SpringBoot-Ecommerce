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
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    @NonNull
    private String email;

    @Column(length = 64, nullable = false)
    @NonNull
    private String password;

    @Column(name = "first_name", length = 45, nullable = false)
    @NonNull
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    @NonNull
    private String lastName;

    private boolean enabled;

    @Column(length = 64)
    private String photos;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Transient
    public void addRole(Role role) {
        this.roles.add(role);
    }

    @Transient
    public String getPhotosImagePath() {
        if(this.id == null || this.photos == null) {
            return "/images/default-user.png";
        }
        return "/user-photos/" + this.id + "/" + this.photos;
    }
}
