package tech.zerofiltre.freeland.application.useCases.serviceContract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StartServiceContractTest {

    public static final Long AGREEMENT_NUMBER = 15L;
    public static final String CLIENT_NAME = "client_name";
    public static final String CLIENT_SIREN = "client_siren";
    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
    public static final String SERVICE_CONTRACT_TERMS = "Service contract terms";
    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String PHONE_NUMBER = "0658425369";
    public static final String FREELANCER_DESCRIPTION = "Zerofiltre freelancer";
    public static final float SERVICE_FEES_RATE = 0.05f;
    StartServiceContract startServiceContract;
    WagePortageAgreementId agreementId = new WagePortageAgreementId(AGREEMENT_NUMBER);
    ClientId clientId = new ClientId(CLIENT_SIREN, CLIENT_NAME);
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Client client = new Client();
    ServiceContract serviceContract;
    WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
    Rate rate = new Rate(700, Currency.EUR, Frequency.DAILY);
    Address address = new Address("2", "Paris", "75010", "Rue du Poulet", "France");


    @Mock
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @Mock
    private ClientProvider clientProvider;
    @Mock
    private ServiceContractProvider serviceContractProvider;

    @Mock
    private ServiceContractNotificationProvider serviceContractNotificationProvider;


    @BeforeEach
    void setUp() {
        startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
                serviceContractProvider, serviceContractNotificationProvider);

        wagePortageAgreement.setWagePortageAgreementId(agreementId);
        Date wagePortageStartDate = new Date();
        wagePortageAgreement.setStartDate(wagePortageStartDate);
        wagePortageAgreement.setServiceFeesRate(SERVICE_FEES_RATE);
        wagePortageAgreement.setAgencyId(agencyId);
        wagePortageAgreement.setFreelancerId(freelancerId);
        wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);

        client.setClientId(clientId);
        client.setAddress(address);
        client.setDescription(FREELANCER_DESCRIPTION);
        client.setPhoneNumber(PHONE_NUMBER);

    }

    @Test
    @DisplayName("Start service contract must return a proper service contract and notify serviceContractStarted")
    void executeStart_mustProduceAProperServiceContract() throws StartServiceContractException {

        //ARRANGE
        when(wagePortageAgreementProvider.wagePortageAgreementOfId(agreementId))
                .thenReturn(Optional.of(wagePortageAgreement));
        when(clientProvider.clientOfId(clientId)).thenReturn(Optional.of(client));
        when(serviceContractProvider.registerContract(any()))
                .thenAnswer(invocationOnMock -> {
                    ServiceContract result = invocationOnMock.getArgument(0);
                    result.setServiceContractId(new ServiceContractId(12L));
                    return result;
                });
        doNothing().when(serviceContractNotificationProvider).notify(any());

        //ACT
        serviceContract = startServiceContract.execute(agreementId, client.getClientId(), SERVICE_CONTRACT_TERMS, rate);

        //ASSERT
        assertThat(serviceContract).isNotNull();

        ServiceContractId contractId = serviceContract.getServiceContractId();
        assertThat(contractId).isNotNull();
        assertThat(contractId.getContractNumber()).isNotNull();

        WagePortageAgreement currentWagePortageAgreement = serviceContract.getWagePortageAgreement();
        assertThat(currentWagePortageAgreement).isEqualTo(wagePortageAgreement);
        assertThat(currentWagePortageAgreement.getStartDate()).isBeforeOrEqualTo(serviceContract.getStartDate());
        assertThat(currentWagePortageAgreement.getEndDate()).isEqualTo(serviceContract.getEndDate());

        ClientId currentClientId = serviceContract.getClientId();
        assertThat(currentClientId).isNotNull();
        assertThat(currentClientId).isEqualTo(clientId);

        assertThat(serviceContract.getServiceContractId()).isNotNull();
        assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

        assertThat(serviceContract.getTerms()).isNotEmpty();
        assertThat(serviceContract.getRate()).isNotNull();
        assertThat(serviceContract.getRate().getValue()).isGreaterThan(0);
        assertThat(serviceContract.getRate().getFrequency()).isNotNull();
        assertThat(serviceContract.getRate().getCurrency()).isNotNull();
        verify(clientProvider, times(1)).clientOfId(any());
        verify(wagePortageAgreementProvider, times(1)).wagePortageAgreementOfId(any());
        verify(serviceContractProvider, times(1)).registerContract(serviceContract);

        ArgumentCaptor<ServiceContractStarted> captor = ArgumentCaptor.forClass(ServiceContractStarted.class);
        verify(serviceContractNotificationProvider, times(1)).notify(captor.capture());
        ServiceContractStarted serviceContractStarted = captor.getValue();
        assertThat(serviceContractStarted.getClientName()).isEqualTo(clientId.getName());
        assertThat(serviceContractStarted.getClientSiren()).isEqualTo(clientId.getSiren());
        assertThat(serviceContractStarted.getFreelancerSiren()).isEqualTo(freelancerId.getSiren());
        assertThat(serviceContractStarted.getFreelancerName()).isEqualTo(freelancerId.getName());
        assertThat(serviceContractStarted.getAgencyName()).isEqualTo(agencyId.getName());
        assertThat(serviceContractStarted.getAgencySiren()).isEqualTo(agencyId.getSiren());
        assertThat(serviceContractStarted.getServiceFeesRate()).isEqualTo(SERVICE_FEES_RATE);
        assertThat(serviceContractStarted.getRateCurrency()).isEqualTo(rate.getCurrency());
        assertThat(serviceContractStarted.getRateValue()).isEqualTo(rate.getValue());
        assertThat(serviceContractStarted.getStartDate()).isEqualTo(serviceContract.getStartDate());
    }

    @Test
    @DisplayName("Start service contract with non existing agreement throws ServiceContract exception")
    void startServiceContract_mustYieldExceptionIfAgreementDoesNotExist() {

        when(wagePortageAgreementProvider.wagePortageAgreementOfId(agreementId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(StartServiceContractException.class)
                .isThrownBy(() -> startServiceContract.execute(agreementId, client.getClientId(), SERVICE_CONTRACT_TERMS, rate));
    }

    @Test
    @DisplayName("When the client does not exist, throw ServiceContract exception")
    void startServiceContract_registerTheClientWhenItDoesNotExist() {

        when(clientProvider.clientOfId(clientId))
                .thenReturn(Optional.empty());

        assertThatExceptionOfType(StartServiceContractException.class)
                .isThrownBy(() -> startServiceContract.execute(agreementId, client.getClientId(), SERVICE_CONTRACT_TERMS, rate));
    }


}