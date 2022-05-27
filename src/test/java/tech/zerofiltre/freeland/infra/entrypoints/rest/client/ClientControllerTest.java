package tech.zerofiltre.freeland.infra.entrypoints.rest.client;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.testcontainers.shaded.com.fasterxml.jackson.core.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.client.model.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @MockBean
    private ClientVMMapper clientVMMapper;
    @MockBean
    private ClientProvider clientProvider;

    ClientVM clientVM = new ClientVM();

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void init() {
        clientVM.setCity("Paris");
        clientVM.setCountry("France");
        clientVM.setDescription("The coolest agency");
        clientVM.setName("Forcos");
        clientVM.setPhoneNumber("0258654585");
        clientVM.setStreetNumber("13");
        clientVM.setStreetName("Rue de la courneuve");
        clientVM.setSiren("45152521");
        clientVM.setPostalCode("75013");
    }


    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(clientProvider.registerClient(any())).thenReturn(Client.builder().clientProvider(clientProvider).build());
        when(clientVMMapper.toVM(any())).thenReturn(clientVM);

        //ACT
        RequestBuilder request = MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(clientVM));

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