package tech.zerofiltre.freeland.infra.entrypoints.rest.agency;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.testcontainers.shaded.com.fasterxml.jackson.core.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.agency.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.agency.model.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AgencyController.class)
class AgencyControllerTest {

    @MockBean
    private AgencyVMMapper agencyVMMapper;
    @MockBean
    private AgencyProvider agencyProvider;

    AgencyVM agencyVM = new AgencyVM();

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void init() {
        agencyVM.setCity("Paris");
        agencyVM.setCountry("France");
        agencyVM.setDescription("The coolest agency");
        agencyVM.setName("Forcos");
        agencyVM.setPhoneNumber("0258654585");
        agencyVM.setStreetNumber("13");
        agencyVM.setStreetName("Rue de la courneuve");
        agencyVM.setSiren("45152521");
        agencyVM.setPostalCode("75013");
    }


    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(agencyProvider.registerAgency(any())).thenReturn(new Agency());
        when(agencyVMMapper.toVM(any())).thenReturn(agencyVM);

        //ACT
        RequestBuilder request = MockMvcRequestBuilders.post("/agency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(agencyVM));

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