package com.floyd.admin.country;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.floyd.admin.FloydBackEndApplication;
import com.floyd.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = FloydBackEndApplication.class)
@AutoConfigureMockMvc
public class CountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "123123123", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/countries/list";

        var mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var countries = objectMapper.readValue(jsonResponse, Country[].class);
        for (Country country : countries) {
            System.out.println(country);
        }

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "123123123", roles = "ADMIN")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        Country country = new Country("Spain", "SP");

        var mvcResult = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(country)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        Integer id =  Integer.parseInt(jsonResponse);
        var savedCountry = entityManager.find(Country.class, id);

        assertThat(savedCountry.getName()).isEqualTo("Spain");
    }

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "123123123", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        Integer id = 18;
        var country = entityManager.find(Country.class, id);
        country.setName("Germany");
        country.setCode("DE");

        var mvcResult = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(country)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(id)))
                .andReturn();

        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var savedCountry = entityManager.find(Country.class, Integer.parseInt(jsonResponse));

        assertThat(savedCountry.getName()).isEqualTo("Germany");
    }

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "123123123", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer id = 18;
        String url = "/countries/delete/" + id;

        mockMvc.perform(get(url)).andExpect(status().isOk());

        var deletedCountry = entityManager.find(Country.class, id);
        assertThat(deletedCountry).isNull();
    }
}
