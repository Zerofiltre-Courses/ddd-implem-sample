package com.zerofiltre.freeland.presentation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerofiltre.freeland.application.FreelanceOrchestrator;
import com.zerofiltre.freeland.domain.Freelancer;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = FreelanceController.class)
public class FreelanceControllerTest {

  public static final String TEST_NAME = "test name";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FreelanceOrchestrator freelanceOrchestrator;

  @Autowired
  private ObjectMapper objectMapper;
  Freelancer freelancer = new Freelancer();

  @BeforeEach
  void init() {
    freelancer.setName(TEST_NAME);
  }

  @Test
  void givenAFreelancer_add_mustReturn200() throws Exception {

    mockMvc.perform(post("/freelance")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(freelancer)))
        .andExpect(status().isOk());
  }

  @Test
  void givenAFreelancer_add_mustReturnAFreelancerWithId() throws Exception {

    Freelancer expectedFreelancer = freelancer;
    expectedFreelancer.setId(12);
    when(freelanceOrchestrator.addFreelancer(freelancer)).thenReturn(expectedFreelancer);

    //when
    MvcResult result = mockMvc.perform(post("/freelance")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(freelancer)))
        .andReturn();

    assertThat(result.getResponse().getContentAsString())
        .isEqualToIgnoringCase(objectMapper.writeValueAsString(expectedFreelancer));
  }

  @Test
  void all_mustReturn200() throws Exception {

    mockMvc.perform(get("/freelance"))
        .andExpect(status().isOk());
  }

  @Test
  void all_mustReturnASetOfFreelancers() throws Exception {

    Freelancer expectedFreelancer = freelancer;
    Freelancer expectedFreelancer2 = freelancer;

    HashSet<Freelancer> expectedResults = new HashSet<>(Arrays.asList(expectedFreelancer, expectedFreelancer2));
    when(freelanceOrchestrator.freelancers()).thenReturn(expectedResults);

    //when
    MvcResult result = mockMvc.perform(get("/freelance"))
        .andReturn();

    assertThat(result.getResponse().getContentAsString())
        .isEqualToIgnoringCase(objectMapper.writeValueAsString(expectedResults));
  }

  @Test
  void all_mustCallFreelancers() throws Exception {

    //when
    mockMvc.perform(get("/freelance"))
        .andReturn();

    //assert
    verify(freelanceOrchestrator, times(1)).freelancers();

  }

  @Test
  void givenAFreelancer_add_mustCallAddFreelancer() throws Exception {

    //when
    mockMvc.perform(post("/freelance")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(freelancer)))
        .andReturn();

    //assert
    ArgumentCaptor<Freelancer> captor = ArgumentCaptor.forClass(Freelancer.class);
    verify(freelanceOrchestrator, times(1)).addFreelancer(captor.capture());
    Freelancer value = captor.getValue();
    assertThat(value.getName()).isEqualTo(TEST_NAME);
  }


}
