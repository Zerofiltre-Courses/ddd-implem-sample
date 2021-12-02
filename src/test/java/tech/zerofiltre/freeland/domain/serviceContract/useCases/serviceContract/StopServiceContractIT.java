package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.kafka.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
public class StopServiceContractIT {

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
    public static final String CLIENT_DESCRIPTION = "Hermes Client";
    public static final String AGENCY_DESCRIPTION = "Procmo Agency";
    ClientId clientId = new ClientId(CLIENT_SIREN, CLIENT_NAME);
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Client client = new Client();
    Freelancer freelancer = new Freelancer();
    Agency agency = new Agency();
    ServiceContract serviceContract;
    WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
    Rate rate = new Rate(700, Currency.EUR, Frequency.DAILY);

    Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
    Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
    Address clientAddress = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");

    StopServiceContract stopServiceContract;

    @Autowired
    ServiceContractProvider serviceContractProvider;
    @Autowired
    WagePortageAgreementProvider wagePortageAgreementProvider;
    StartServiceContract startServiceContract;

    @MockBean
    ServiceContractNotificationProvider serviceContractNotificationProvider;

    @Autowired
    AgencyProvider agencyProvider;

    @Autowired
    FreelancerProvider freelancerProvider;
    @Autowired
    ClientProvider clientProvider;

    @BeforeAll
    void init() {
        startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
                serviceContractProvider, serviceContractNotificationProvider);
        stopServiceContract = new StopServiceContract(serviceContractProvider);
    }

    @Test
    @DisplayName("StopServiceContract must delete the service contract and the related wage portage agreement ")
    void execute() throws StartServiceContractException, StopServiceContractException {

        //ARRANGE : Save a wage portage agreement and a service contract
        createAServiceContract();
        doNothing().when(serviceContractNotificationProvider).notify(any());

        //ACT
        stopServiceContract.execute(serviceContract);

        //ASSERT
        assertThat(serviceContractProvider.serviceContractOfId(serviceContract.getServiceContractId())).isEmpty();
        assertThat(wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreement.getWagePortageAgreementId()))
                .isEmpty();
    }

    private void createAServiceContract() throws StartServiceContractException {
        Date wagePortageStartDate = new Date();
        wagePortageAgreement.setStartDate(wagePortageStartDate);
        wagePortageAgreement.setServiceFeesRate(0.05f);
        wagePortageAgreement.setAgencyId(agencyId);
        wagePortageAgreement.setFreelancerId(freelancerId);
        wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);

        agency.setAgencyId(agencyId);
        agency.setAddress(agencyAddress);
        agency.setDescription(AGENCY_DESCRIPTION);
        agency.setPhoneNumber(PHONE_NUMBER);
        agency = agencyProvider.registerAgency(agency);

        freelancer.setFreelancerId(freelancerId);
        freelancer.setAddress(freelancerAddress);
        freelancer.setDescription(FREELANCER_DESCRIPTION);
        freelancer.setPhoneNumber(PHONE_NUMBER);
        freelancer = freelancerProvider.registerFreelancer(freelancer);

        wagePortageAgreement.setFreelancerId(freelancer.getFreelancerId());
        wagePortageAgreement.setAgencyId(agency.getAgencyId());
        wagePortageAgreement = wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
        assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotNull();

        client.setClientId(clientId);
        client.setAddress(clientAddress);
        client.setDescription(CLIENT_DESCRIPTION);
        client.setPhoneNumber(PHONE_NUMBER);
        clientProvider.registerClient(client);

        serviceContract = startServiceContract
                .execute(wagePortageAgreement.getWagePortageAgreementId(), client.getClientId(), SERVICE_CONTRACT_TERMS, rate);
        assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

    }

}
