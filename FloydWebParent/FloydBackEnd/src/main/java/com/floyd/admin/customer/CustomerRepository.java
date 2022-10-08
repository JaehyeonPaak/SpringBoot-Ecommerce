package com.floyd.admin.customer;

import com.floyd.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE CONCAT(" +
            "c.id, ' ', " +
            "c.firstName, ' ', " +
            "c.lastName, ' '," +
            "c.email, ' '," +
            "c.city, ' '," +
            "c.state, ' '," +
            "c.country) LIKE %:keyword%")
    public Page<Customer> findAll(@Param("keyword") String keyword, Pageable pageable);

    public Page<Customer> findAll(Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = :enabled WHERE c.id = :id")
    @Modifying
    public void updateEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);

    public Long countById(Integer id);

    public Customer findByEmail(String email);
}
