package tech.zerofiltre.freeland.application.servicecontract;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.mapper.*;
import tech.zerofiltre.freeland.infra.providers.database.client.*;
import tech.zerofiltre.freeland.infra.providers.database.client.mapper.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.mapper.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import({
        WagePortageAgreementDatabaseProvider.class,
        WagePortageAgreementJPAMapperImpl.class,
        FreelancerDatabaseProvider.class,
        FreelancerJPAMapperImpl.class,
        AgencyDatabaseProvider.class,
        AgencyJPAMapperImpl.class,
        ClientDatabaseProvider.class,
        ClientJPAMapperImpl.class,
        ServiceContractDatabaseProvider.class,
        ServiceContractJPAMapperImpl.class})
class ServiceContractStarterIT {

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
    ServiceContractStarter serviceContractStarter;
    ClientId clientId = new ClientId(CLIENT_SIREN, CLIENT_NAME);
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Client client;
    Freelancer freelancer;
    Agency agency;
    ServiceContract serviceContract;
    WagePortageAgreement wagePortageAgreement;
    Rate rate = new Rate(700, Currency.EUR, Frequency.DAILY);
    Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
    Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
    Address clientAddress = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");


    @Autowired
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @Autowired
    private ClientProvider clientProvider;

    @Autowired
    private AgencyProvider agencyProvider;

    @Autowired
    private FreelancerProvider freelancerProvider;

    @Autowired
    private ServiceContractProvider serviceContractProvider;

    @MockBean
    private ServiceContractNotificationProvider serviceContractNotificationProvider;


    @BeforeAll
    void setUp() {
        serviceContractStarter = new ServiceContractStarter(clientProvider, wagePortageAgreementProvider,
                serviceContractProvider, serviceContractNotificationProvider);
        client = Client.builder().clientProvider(clientProvider).build();
        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider).build();
        agency = Agency.builder().agencyProvider(agencyProvider).build();
        serviceContract = ServiceContract.builder().serviceContractProvider(serviceContractProvider).build();
        wagePortageAgreement = WagePortageAgreement.builder().wagePortageAgreementProvider(wagePortageAgreementProvider).build();

    }

    @Test
    @DisplayName("Start service contract must return a proper service contract containing all needed info")
    void executeStart_mustProduceAProperServiceContract() throws StartServiceContractException {


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

        //arrange : A wage portage agreement is required for any service contract signature
        Date wagePortageStartDate = new Date();
        wagePortageAgreement = WagePortageAgreement.builder().copy(wagePortageAgreement)
                .startDate(wagePortageStartDate)
                .serviceFeesRate(0.05f)
                .agencyId(agencyId)
                .freelancerId(freelancerId)
                .terms(WAGE_PORTAGE_TERMS)
                .build();
        wagePortageAgreement = wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
        assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotZero();

        //A client data is required, registered or not
        client = Client.builder().clientProvider(clientProvider)
                .clientId(clientId)
                .address(clientAddress)
                .description(CLIENT_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        clientProvider.registerClient(client);

        doNothing().when(serviceContractNotificationProvider).notify(any());

        //when
        serviceContract = serviceContractStarter
                .start(wagePortageAgreement.getWagePortageAgreementId(), client.getClientId(), SERVICE_CONTRACT_TERMS, rate);

        //then
        assertThat(serviceContract).isNotNull();

        //the service contract is correctly created
        ServiceContractId contractId = serviceContract.getServiceContractId();
        assertThat(contractId).isNotNull();
        assertThat(contractId.getContractNumber()).isNotNull();

        //The Wage portage agreement attached to the service contract is the previously saved one
        WagePortageAgreement currentWagePortageAgreement = serviceContract.getWagePortageAgreement();
        assertThat(currentWagePortageAgreement.getWagePortageAgreementId().getAgreementNumber())
                .isEqualTo(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber());

        assertThat(currentWagePortageAgreement.getStartDate()).isBeforeOrEqualTo(serviceContract.getStartDate());

        //the service contract is attached with a registered client
        ClientId currentClientId = serviceContract.getClientId();
        assertThat(currentClientId).isNotNull();
        Assertions.assertThat(currentClientId.getSiren()).isEqualTo(clientId.getSiren());
        Assertions.assertThat(currentClientId.getName()).isEqualTo(clientId.getName());

        //the attached registered has been created properly
        Optional<Client> registeredClient = clientProvider.clientOfId(currentClientId);
        assertThat(registeredClient).isNotEmpty();
        registeredClient.ifPresent(theRegisteredClient -> {
            Assertions.assertThat(theRegisteredClient.getAddress().getCity()).isEqualTo(clientAddress.getCity());
            Assertions.assertThat(theRegisteredClient.getAddress().getStreetName()).isEqualTo(clientAddress.getStreetName());
            Assertions.assertThat(theRegisteredClient.getAddress().getStreetNumber())
                    .isEqualTo(clientAddress.getStreetNumber());
            Assertions.assertThat(theRegisteredClient.getAddress().getPostalCode()).isEqualTo(clientAddress.getPostalCode());
            Assertions.assertThat(theRegisteredClient.getAddress().getCountry()).isEqualTo(clientAddress.getCountry());
        });

        //the service contract has all  the needed info
        assertThat(serviceContract.getServiceContractId()).isNotNull();
        assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

        assertThat(serviceContract.getTerms()).isNotEmpty();
        assertThat(serviceContract.getRate()).isNotNull();
        assertThat(serviceContract.getRate().getValue()).isGreaterThan(0);
        assertThat(serviceContract.getRate().getFrequency()).isNotNull();
        assertThat(serviceContract.getRate().getCurrency()).isNotNull();

        //the service contract is attached to the agency attached to the Wage portage agreement
        AgencyId registeredAgencyId = serviceContract.getWagePortageAgreement().getAgencyId();
        Assertions.assertThat(registeredAgencyId.getName()).isEqualTo(agencyId.getName());
        Assertions.assertThat(registeredAgencyId.getSiren()).isEqualTo(agencyId.getSiren());

        Optional<Agency> registeredAgency = agencyProvider.agencyOfId(registeredAgencyId);
        assertThat(registeredAgency).isNotEmpty();
        registeredAgency.ifPresent(theRegisteredAgency -> {
            Assertions.assertThat(theRegisteredAgency.getAddress().getCity()).isEqualTo(agencyAddress.getCity());
            Assertions.assertThat(theRegisteredAgency.getAddress().getStreetName()).isEqualTo(agencyAddress.getStreetName());
            Assertions.assertThat(theRegisteredAgency.getAddress().getStreetNumber())
                    .isEqualTo(agencyAddress.getStreetNumber());
            Assertions.assertThat(theRegisteredAgency.getAddress().getPostalCode()).isEqualTo(agencyAddress.getPostalCode());
            Assertions.assertThat(theRegisteredAgency.getAddress().getCountry()).isEqualTo(agencyAddress.getCountry());
        });

        //the service contract is attached to the freelancer attached to the Wage portage agreement
        FreelancerId registeredFreelancerId = serviceContract.getWagePortageAgreement().getFreelancerId();
        assertThat(registeredFreelancerId.getName()).isEqualTo(freelancerId.getName());
        assertThat(registeredFreelancerId.getSiren()).isEqualTo(freelancerId.getSiren());

        Optional<Freelancer> registeredFreelancer = freelancerProvider.freelancerOfId(registeredFreelancerId);
        assertThat(registeredFreelancer).isNotEmpty();
        registeredFreelancer.ifPresent(theRegisteredFreelancer -> {
            assertThat(theRegisteredFreelancer.getAddress().getCity()).isEqualTo(freelancerAddress.getCity());
            assertThat(theRegisteredFreelancer.getAddress().getStreetName()).isEqualTo(freelancerAddress.getStreetName());
            assertThat(theRegisteredFreelancer.getAddress().getStreetNumber()).isEqualTo(freelancerAddress.getStreetNumber());
            assertThat(theRegisteredFreelancer.getAddress().getPostalCode()).isEqualTo(freelancerAddress.getPostalCode());
            assertThat(theRegisteredFreelancer.getAddress().getCountry()).isEqualTo(freelancerAddress.getCountry());
        });
    }

}