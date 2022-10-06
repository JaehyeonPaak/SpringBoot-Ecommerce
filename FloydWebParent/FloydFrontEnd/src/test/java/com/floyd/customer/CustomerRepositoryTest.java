package com.floyd.customer;

import com.floyd.common.entity.Country;
import com.floyd.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer countryId = 13;
        var country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Hugo");
        customer.setLastName("Loris");
        customer.setPassword("123123123");
        customer.setEmail("hugo@gmail.com");
        customer.setPhoneNumber("010-4703-1231");
        customer.setAddressLine1("1232 Tottenham");
        customer.setCity("London");
        customer.setState("England");
        customer.setPostalCode("18593");
        customer.setCreatedTime(new Date());

        var savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2() {
        Integer countryId = 13;
        var country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("Eric");
        customer.setLastName("Dier");
        customer.setPassword("123123123");
        customer.setEmail("eric@gmail.com");
        customer.setPhoneNumber("010-1352-1231");
        customer.setAddressLine1("1431 Tottenham");
        customer.setCity("London");
        customer.setState("England");
        customer.setPostalCode("18593");
        customer.setCreatedTime(new Date());

        var savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers() {
        var customers = customerRepository.findAll();
        customers.forEach(customer -> {
            System.out.println(customer.getFirstName());
        });
        assertThat(customers).hasSizeGreaterThan(0);
    }

    @Test
    public void testFindCustomerByEmail() {
        String email = "eric@gmail.com";
        var customer = customerRepository.findByEmail(email);
        assertThat(customer.getFirstName()).isEqualTo("Eric");
    }

    @Test
    public void testEnabled() {
        Integer id = 1;
        customerRepository.enable(id);
        var customer = entityManager.find(Customer.class, id);
        assertThat(customer.isEnabled()).isTrue();
    }
}
