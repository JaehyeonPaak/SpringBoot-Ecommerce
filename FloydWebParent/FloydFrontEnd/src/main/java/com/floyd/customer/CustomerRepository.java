package com.floyd.customer;

import com.floyd.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    public Customer findByEmail(String email);

    public Customer findByVerificationCode(String verificationCode);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = :id")
    @Modifying
    @Transactional
    public void enable(@Param("id") Integer id);
}
