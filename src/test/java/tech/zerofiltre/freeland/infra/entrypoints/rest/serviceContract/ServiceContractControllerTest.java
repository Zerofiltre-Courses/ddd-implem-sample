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
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
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
    WagePortageAgreementVM agreementVM = new WagePortageAgreementVM();
    WagePortageAgreement wagePortageAgreement;
    ServiceContractVM serviceContractVM = new ServiceContractVM();
    ServiceContract serviceContract;
    @MockBean
    ServiceContractVMMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientProvider clientProvider;
    @MockBean
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @MockBean
    private ServiceContractProvider serviceContractProvider;
    @MockBean
    private ServiceContractNotificationProvider notificationProvider;

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @BeforeEach
    void init() {
        agreementVM.setAgreementNumber(AGREEMENT_NUMBER);
        agreementVM.setAgencySiren(AGENCY_SIREN);
        agreementVM.setFreelancerSiren(FREELANCER_SIREN);
        agreementVM.setTerms(AGREEMENT_TERMS);
        agreementVM.setServiceFeesRate(SERVICE_FEES_RATE);
        AgencyId agencyId = new AgencyId(AGENCY_SIREN, "");
        FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, "");


        wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .wagePortageAgreementId(new WagePortageAgreementId(AGREEMENT_NUMBER))
                .agencyId(agencyId)
                .freelancerId(freelancerId)
                .terms(AGREEMENT_TERMS)
                .serviceFeesRate(SERVICE_FEES_RATE)
                .build();

        serviceContractVM.setContractNumber(CONTRACT_NUMBER);
        serviceContractVM.setClientSiren(CLIENT_SIREN);
        serviceContractVM.setTerms(CONTRACT_TERMS);
        serviceContractVM.setRateCurrency(Rate.Currency.EUR);
        serviceContractVM.setRateFrequency(Rate.Frequency.DAILY);
        serviceContractVM.setRateValue(RATE_VALUE);
        serviceContractVM.setWagePortageAgreementNumber(AGREEMENT_NUMBER);

        WagePortageAgreement wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementId(new WagePortageAgreementId(AGREEMENT_NUMBER))
                .freelancerId(freelancerId)
                .agencyId(agencyId)
                .serviceFeesRate(SERVICE_FEES_RATE)
                .terms(AGREEMENT_TERMS)
                .build();

        serviceContract = ServiceContract.builder()
                .serviceContractId(new ServiceContractId(CONTRACT_NUMBER))
                .serviceContractProvider(serviceContractProvider)
                .rate(new Rate(RATE_VALUE, Rate.Currency.EUR, Rate.Frequency.DAILY))
                .terms(CONTRACT_TERMS)
                .clientId(new ClientId(CLIENT_SIREN, ""))
                .wagePortageAgreement(wagePortageAgreement).build();


    }

    @Test
    void whenValidInput_thenReturn200() throws Exception {

        //ARRANGE
        when(clientProvider.clientOfId(any()))
                .thenReturn(Optional.of(Client.builder().clientProvider(clientProvider).build()));
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
}