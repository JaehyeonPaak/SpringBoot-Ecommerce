package com.floyd.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "settings")
public class Setting {

    @Id
    @Column(name = "`key`", nullable = false, length = 128)
    private String key;

    @Column(nullable = false, length = 1024)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 45)
    private SettingCategory category;

    public Setting(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Setting setting)) return false;
        return Objects.equals(key, setting.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return "Setting{" + "key='" + key + '\'' + ", value='" + value + "'}";
    }
}
