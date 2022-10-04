package com.floyd.setting;

import com.floyd.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {

    @Autowired
    private SettingRepository settingRepository;

    @Test
    public void testFindByTwoCategories() {
        var settings = settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
        settings.forEach(System.out::println);
    }
}
