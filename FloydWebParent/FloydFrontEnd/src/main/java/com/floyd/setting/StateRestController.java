package com.floyd.setting;

import com.floyd.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<State> listByCountry(@PathVariable(name = "id") Integer id) {
        var country = countryRepository.findById(id).get();
        return stateRepository.findByCountryOrderByNameAsc(country);
    }
}
