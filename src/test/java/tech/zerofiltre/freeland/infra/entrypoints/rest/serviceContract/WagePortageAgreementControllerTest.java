package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract;

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
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WagePortageAgreementController.class)
class WagePortageAgreementControllerTest {

    public static final String AGENCY_SIREN = "agency siren";
    public static final String FREELANCER_SIREN = "freelancer siren";
    public static final String AGREEMENT_TERMS = "agreement terms";
    public static final float SERVICE_FEES_RATE = 0.05f;
    WagePortageAgreementVM agreementVM = new WagePortageAgreementVM();
    WagePortageAgreement wagePortageAgreement;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WagePortageAgreementVMMapper mapper;
    @MockBean
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @MockBean
    private AgencyProvider agencyProvider;
    @MockBean
    private FreelancerProvider freelancerProvider;

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @BeforeEach
    void init() {
        agreementVM.setAgreementNumber(12);
        agreementVM.setAgencySiren(AGENCY_SIREN);
        agreementVM.setFreelancerSiren(FREELANCER_SIREN);
        agreementVM.setTerms(AGREEMENT_TERMS);
        agreementVM.setServiceFeesRate(SERVICE_FEES_RATE);

        wagePortageAgreement = WagePortageAgreement.builder().wagePortageAgreementProvider(wagePortageAgreementProvider)
                .wagePortageAgreementId(new WagePortageAgreementId(12L))
                .agencyId(new AgencyId(AGENCY_SIREN, ""))
                .freelancerId(new FreelancerId(FREELANCER_SIREN, ""))
                .terms(AGREEMENT_TERMS)
                .serviceFeesRate(SERVICE_FEES_RATE)
                .build();
    }

    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(Agency.builder().agencyProvider(agencyProvider).build()));
        when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.of(Freelancer.builder().freelancerProvider(freelancerProvider).build()));
        when(wagePortageAgreementProvider.registerWagePortageAgreement(any())).thenReturn(wagePortageAgreement);

        when(mapper.toVM(any())).thenReturn(agreementVM);
        when(mapper.fromVM(agreementVM)).thenReturn(wagePortageAgreement);

        //ACT
        RequestBuilder request = MockMvcRequestBuilders.post("/wage-portage-agreement/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(agreementVM));

        //ASSERT
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.agreementNumber").value("12"));
    }
}