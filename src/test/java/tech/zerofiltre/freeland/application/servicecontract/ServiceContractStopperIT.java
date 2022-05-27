package tech.zerofiltre.freeland.application.servicecontract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.kafka.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.Rate.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
class ServiceContractStopperIT {

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
    Client client;
    Freelancer freelancer;
    ServiceContract serviceContract;
    WagePortageAgreement wagePortageAgreement;
    Rate rate = new Rate(700, Rate.Currency.EUR, Frequency.DAILY);
    Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
    Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
    Address clientAddress = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");
    ServiceContractStopper serviceContractStopper;
    @Autowired
    ServiceContractProvider serviceContractProvider;
    @Autowired
    WagePortageAgreementProvider wagePortageAgreementProvider;
    ServiceContractStarter serviceContractStarter;
    @MockBean
    ServiceContractNotificationProvider serviceContractNotificationProvider;
    @Autowired
    AgencyProvider agencyProvider;
    Agency agency;
    @Autowired
    FreelancerProvider freelancerProvider;
    @Autowired
    ClientProvider clientProvider;

    @BeforeAll
    void init() {
        serviceContractStarter = new ServiceContractStarter(clientProvider, wagePortageAgreementProvider,
                serviceContractProvider, serviceContractNotificationProvider);
        serviceContractStopper = new ServiceContractStopper();

        client = Client.builder().clientProvider(clientProvider).build();
        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider).build();
        agency = Agency.builder().agencyProvider(agencyProvider).build();
        wagePortageAgreement = WagePortageAgreement.builder().wagePortageAgreementProvider(wagePortageAgreementProvider).build();
    }

    @Test
    @DisplayName("StopServiceContract must delete the service contract and the related wage portage agreement ")
    void execute() throws StartServiceContractException, StopServiceContractException {

        //ARRANGE : Save a wage portage agreement and a service contract
        createAServiceContract();
        doNothing().when(serviceContractNotificationProvider).notify(any());

        //ACT
        serviceContractStopper.execute(serviceContract);

        //ASSERT
        assertThat(serviceContractProvider.serviceContractOfId(serviceContract.getServiceContractId())).isEmpty();
        assertThat(wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreement.getWagePortageAgreementId()))
                .isEmpty();
    }

    private void createAServiceContract() throws StartServiceContractException {
        agency = Agency.builder().agencyProvider(agencyProvider)
                .agencyId(agencyId)
                .address(agencyAddress)
                .description(AGENCY_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        agency = agencyProvider.registerAgency(agency);


        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider)
                .freelancerId(freelancerId)
                .address(freelancerAddress)
                .description(FREELANCER_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        freelancer = freelancerProvider.registerFreelancer(freelancer);

        Date wagePortageStartDate = new Date();
        wagePortageAgreement = WagePortageAgreement.builder()
                .copy(wagePortageAgreement)
                .startDate(wagePortageStartDate)
                .serviceFeesRate(0.05f)
                .agencyId(agency.getAgencyId())
                .freelancerId(freelancer.getFreelancerId())
                .terms(WAGE_PORTAGE_TERMS)
                .build();
        wagePortageAgreement = wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
        assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotZero();


        client = Client.builder().clientProvider(clientProvider)
                .clientId(clientId)
                .address(clientAddress)
                .description(CLIENT_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        clientProvider.registerClient(client);


        serviceContract = serviceContractStarter
                .start(wagePortageAgreement.getWagePortageAgreementId(), client.getClientId(), SERVICE_CONTRACT_TERMS, rate);
        assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

    }

}
