package com.floyd.admin.user.state;

import com.floyd.admin.user.country.CountryRepository;
import com.floyd.common.entity.Country;
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

    @GetMapping("/states/list/{id}")
    public List<State> listByCountry(@PathVariable(name = "id") Integer id) {
        var country = countryRepository.findById(id).get();
        return stateRepository.findByCountryOrderByNameAsc(country);
    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state) {
        var savedState = stateRepository.save(state);
        return String.valueOf(savedState.getId());
    }

    @GetMapping("/states/delete/{id}")
    public void delete(@PathVariable(name = "id") Integer id) {
        stateRepository.deleteById(id);
    }
}
