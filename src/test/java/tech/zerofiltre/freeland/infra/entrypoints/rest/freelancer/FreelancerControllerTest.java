package tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.testcontainers.shaded.com.fasterxml.jackson.core.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.model.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FreelancerController.class)
class FreelancerControllerTest {

    @MockBean
    private FreelancerVMMapper freelancerVMMapper;
    @MockBean
    private FreelancerProvider freelancerProvider;

    FreelancerVM freelancerVM = new FreelancerVM();

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void init() {
        freelancerVM.setCity("Paris");
        freelancerVM.setCountry("France");
        freelancerVM.setDescription("The coolest agency");
        freelancerVM.setName("Forcos");
        freelancerVM.setPhoneNumber("0258654585");
        freelancerVM.setStreetNumber("13");
        freelancerVM.setStreetName("Rue de la courneuve");
        freelancerVM.setSiren("45152521");
        freelancerVM.setPostalCode("75013");
    }


    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(freelancerProvider.registerFreelancer(any())).thenReturn(Freelancer.builder().freelancerProvider(freelancerProvider).build());
        when(freelancerVMMapper.toVM(any())).thenReturn(freelancerVM);

        //ACT
        RequestBuilder request = MockMvcRequestBuilders.post("/freelancer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(freelancerVM));

        //ASSERT
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.postalCode").value("75013"));

    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}