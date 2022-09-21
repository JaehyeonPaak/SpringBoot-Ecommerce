package com.floyd.admin.user;

import com.floyd.admin.user.user.RoleRepository;
import com.floyd.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole() {
        var roleAdmin = new Role("Admin", "Manage everything");
        var savedRole = roleRepository.save(roleAdmin);

        // to check if the first element is saved in the database correctly...
        assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles() {
        var roleSalesperson = new Role("Salesperson", "Manage product price, customers, shipping, orders, and sales report");
        var roleEditor = new Role("Editor", "Manage categories, brands, products, articles, and menus");
        var roleShipper = new Role("Shipper", "View products, orders, and update order status");
        var roleAssistant = new Role("Assistant", "Manage questions and reviews");

        List<Role> roles = Arrays.asList(roleSalesperson, roleEditor, roleShipper, roleAssistant);
        roleRepository.saveAll(roles);
    }
}
