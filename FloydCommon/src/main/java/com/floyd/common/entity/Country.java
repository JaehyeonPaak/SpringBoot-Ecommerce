package com.floyd.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 4)
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
}
