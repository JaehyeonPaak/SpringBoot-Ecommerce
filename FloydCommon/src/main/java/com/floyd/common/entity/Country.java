package com.floyd.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Integer id;

    @Column(nullable = false, length = 45)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false, length = 4)
    @Getter
    @Setter
    private String code;

    @OneToMany(mappedBy = "country")
    private Set<State> states;

    public Country(String name, String code, Set<State> state) {
        this.name = name;
        this.code = code;
        this.states = state;
    }

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
