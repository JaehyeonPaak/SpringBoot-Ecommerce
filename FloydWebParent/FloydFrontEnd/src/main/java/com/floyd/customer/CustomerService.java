package com.floyd.customer;

import com.floyd.common.entity.Country;
import com.floyd.common.entity.Customer;
import com.floyd.setting.CountryRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        var customer = customerRepository.findByEmail(email);
        return customer == null;
    }

    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());
        var randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        var encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }
}
