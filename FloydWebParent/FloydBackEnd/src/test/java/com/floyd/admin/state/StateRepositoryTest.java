package com.floyd.admin.state;

import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.state.StateRepository;
import com.floyd.common.entity.Country;
import com.floyd.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateStatesInUSA() {
        Integer id = 13;
        var country = entityManager.find(Country.class, id);
        var states = List.of(new State("California", country), new State("Washington", country), new State("Texas", country));
        stateRepository.saveAll(states);
//        country.setStates(Set.of(state));
//        assertThat(country.getStates()).size().isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry() {
        Integer id = 13;
        var country = entityManager.find(Country.class, id);
        var states = stateRepository.findByCountryOrderByNameAsc(country);
        states.forEach(state -> {
            System.out.println(state.getName());
        });
    }
}
