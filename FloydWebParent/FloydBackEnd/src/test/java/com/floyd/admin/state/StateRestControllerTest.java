package com.floyd.admin.state;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floyd.admin.user.FloydBackEndApplication;
import com.floyd.admin.user.state.StateRepository;
import com.floyd.common.entity.Country;
import com.floyd.common.entity.State;
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
public class StateRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "idontknow", roles = "GOD")
    public void testListStatesByCountry() throws Exception {
        Integer countryId = 13;
        String url = "/states/list/" + countryId;
        var mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var states = objectMapper.readValue(jsonResponse, State[].class);

        for (State state : states) {
            System.out.println(state.getName());
        }
    }

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "idontknow", roles = "GOD")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        var country = entityManager.find(Country.class, 13);
        State state = new State("Arizona", country);
        var mvcResult = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state)).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var savedState = entityManager.find(State.class, Integer.parseInt(jsonResponse));
        System.out.println(savedState.getName());
        assertThat(savedState.getCountry().getName()).isEqualTo(country.getName());
    }

    @Test
    @WithMockUser(username = "kendall@gmail.com", password = "idontknow", roles = "GOD")
    public void testDeleteState() throws Exception {
        Integer stateId = 5;
        String url = "/states/delete/" + stateId;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print());
        var deletedState = entityManager.find(State.class, stateId);
        assertThat(deletedState).isNull();
    }
}
