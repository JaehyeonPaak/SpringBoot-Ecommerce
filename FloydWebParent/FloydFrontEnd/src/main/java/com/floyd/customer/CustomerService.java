package com.floyd.customer;

import com.floyd.common.entity.Country;
import com.floyd.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
