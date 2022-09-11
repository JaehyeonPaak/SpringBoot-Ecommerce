package com.floyd.admin;

import com.floyd.admin.user.UserRepository;
import com.floyd.common.entity.Role;
import com.floyd.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUserWithOneRole() {
        User user1 = new User("conor@gmail.com", "1234", "Conor", "Mcgregor");
        var roleAdmin = entityManager.find(Role.class, 1);
        user1.addRole(roleAdmin);

        var savedUser = userRepository.save(user1);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRole() {
        User user2 = new User("nate@gmail.com", "1122", "Nate", "Diaz");
        var roleEditor = entityManager.find(Role.class, 3);
        var roleAssistant = entityManager.find(Role.class, 5);
        user2.addRole(roleEditor);
        user2.addRole(roleAssistant);

        var savedUser = userRepository.save(user2);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers() {
        var users = userRepository.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

}
