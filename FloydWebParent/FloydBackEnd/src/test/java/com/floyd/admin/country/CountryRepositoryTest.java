package com.floyd.admin.country;

import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.country.CountryRepository;
import com.floyd.common.entity.Country;
import com.floyd.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testFindAllByOrderByNameAsc() {
        Country USA = new Country("United States of America", "US");
        Country Korea = new Country("Korea", "KR");
        Country Canada = new Country("Canada", "CA");
        Country Japan = new Country("Japan", "JP");
        countryRepository.saveAll(List.of(USA, Korea, Canada, Japan));

        var countries = countryRepository.findAllByOrderByNameAsc();
        countries.forEach(country -> {
            System.out.println(country.getName());
        });
    }
}
