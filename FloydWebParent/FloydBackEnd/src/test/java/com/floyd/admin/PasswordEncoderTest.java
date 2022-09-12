package com.floyd.admin;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = "helloworld1234";
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        System.out.println(encodedPassword);

        Boolean result = bCryptPasswordEncoder.matches(password, encodedPassword);
        assertThat(result).isTrue();
    }
}
