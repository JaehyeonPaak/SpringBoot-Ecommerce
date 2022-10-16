package com.floyd.admin.customer;

import com.floyd.admin.country.CountryRepository;
import com.floyd.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CustomerService {

    public static final int CUSTOMERS_PER_PAGE = 8;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);
        if (keyword != null) {
            return customerRepository.findAll(keyword, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException {
        try {
            return customerRepository.findById(id).get();
        }
        catch (NoSuchElementException e) {
            throw new CustomerNotFoundException("Could not find any customers with ID " + id);
        }
    }

    public void delete(Integer id) throws CustomerNotFoundException {
        var count = customerRepository.countById(id);
        if (count == 0 || count == null) {
            throw new CustomerNotFoundException("Could not find any customers with ID " + id);
        }
        customerRepository.deleteById(id);
    }

    public boolean isEmailUnique(Integer id, String email) {
        var customer = customerRepository.findByEmail(email);
        if (customer.getId() == id) {
            return true;
        }
        if (customer != null || customer.getId() != id) {
            return false;
        }
        return true;
    }

    public void save(Customer customer) {
        var customerId = customer.getId();
        var customerInDB = customerRepository.findById(customerId).get();

        if (!customer.getPassword().isEmpty() || customer.getPassword() != null) {
            var password = customer.getPassword();
            var encodedPassword = passwordEncoder.encode(password);
            customer.setPassword(encodedPassword);
        }
        else {
            var password = customerInDB.getPassword();
            customer.setPassword(password);
        }
        customer.setEnabled(customerInDB.isEnabled());
        customer.setCreatedTime(customerInDB.getCreatedTime());
        customerRepository.save(customer);
    }
}
