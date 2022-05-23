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
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.application.useCases.serviceContract.*;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServiceContractController.class)
class ServiceContractControllerTest {

    public static final String AGENCY_SIREN = "agency siren";
    public static final String FREELANCER_SIREN = "freelancer siren";
    public static final String CLIENT_SIREN = "client siren";
    public static final String AGREEMENT_TERMS = "agreement terms";
    public static final String CONTRACT_TERMS = "contract terms";
    public static final float SERVICE_FEES_RATE = 0.05f;
    public static final long AGREEMENT_NUMBER = 12L;
    public static final long CONTRACT_NUMBER = 13;
    public static final int RATE_VALUE = 700;
    @Autowired
    private MockMvc mockMvc;

    WagePortageAgreementVM agreementVM = new WagePortageAgreementVM();
    WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();

    ServiceContractVM serviceContractVM = new ServiceContractVM();
    ServiceContract serviceContract = new ServiceContract();

    @MockBean
    ServiceContractVMMapper mapper;

    @MockBean
    private ClientProvider clientProvider;
    @MockBean
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @MockBean
    private ServiceContractProvider serviceContractProvider;
    @MockBean
    private ServiceContractNotificationProvider notificationProvider;


    @BeforeEach
    void init() {
        agreementVM.setAgreementNumber(AGREEMENT_NUMBER);
        agreementVM.setAgencySiren(AGENCY_SIREN);
        agreementVM.setFreelancerSiren(FREELANCER_SIREN);
        agreementVM.setTerms(AGREEMENT_TERMS);
        agreementVM.setServiceFeesRate(SERVICE_FEES_RATE);

        wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(AGREEMENT_NUMBER));
        AgencyId agencyId = new AgencyId(AGENCY_SIREN, "");
        wagePortageAgreement.setAgencyId(agencyId);
        FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, "");
        wagePortageAgreement.setFreelancerId(freelancerId);
        wagePortageAgreement.setTerms(AGREEMENT_TERMS);
        wagePortageAgreement.setServiceFeesRate(SERVICE_FEES_RATE);

        serviceContractVM.setContractNumber(CONTRACT_NUMBER);
        serviceContractVM.setClientSiren(CLIENT_SIREN);
        serviceContractVM.setTerms(CONTRACT_TERMS);
        serviceContractVM.setRateCurrency(Rate.Currency.EUR);
        serviceContractVM.setRateFrequency(Rate.Frequency.DAILY);
        serviceContractVM.setRateValue(RATE_VALUE);
        serviceContractVM.setWagePortageAgreementNumber(AGREEMENT_NUMBER);

        serviceContract.setServiceContractId(new ServiceContractId(CONTRACT_NUMBER));
        serviceContract.setRate(new Rate(RATE_VALUE, Rate.Currency.EUR, Rate.Frequency.DAILY));
        serviceContract.setTerms(CONTRACT_TERMS);
        serviceContract.setClientId(new ClientId(CLIENT_SIREN, ""));
        serviceContract.setWagePortageAgreement(new WagePortageAgreement(
                new WagePortageAgreementId(AGREEMENT_NUMBER),
                freelancerId,
                agencyId,
                SERVICE_FEES_RATE,
                AGREEMENT_TERMS,
                null,
                null
        ));


    }


    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(clientProvider.clientOfId(any())).thenReturn(Optional.of(new Client()));
        when(wagePortageAgreementProvider.wagePortageAgreementOfId(any())).thenReturn(Optional.of(wagePortageAgreement));
        doNothing().when(notificationProvider).notify(any());
        when(serviceContractProvider.registerContract(any())).thenReturn(serviceContract);
        when(mapper.toVM(any())).thenReturn(serviceContractVM);
        when(mapper.fromVM(serviceContractVM)).thenReturn(serviceContract);

        //ACT
        RequestBuilder request = MockMvcRequestBuilders.post("/service-contract/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(serviceContractVM));

        //ASSERT
        mockMvc.perform(request)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.contractNumber").value(CONTRACT_NUMBER));
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}