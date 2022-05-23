package tech.zerofiltre.freeland.application.useCases.serviceContract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.Date;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import tech.zerofiltre.freeland.domain.Address;
import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.Frequency;
import tech.zerofiltre.freeland.domain.agency.AgencyProvider;
import tech.zerofiltre.freeland.domain.agency.model.Agency;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import tech.zerofiltre.freeland.domain.client.ClientProvider;
import tech.zerofiltre.freeland.domain.client.model.Client;
import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import tech.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.WagePortageAgreementProvider;
import tech.zerofiltre.freeland.infra.providers.database.agency.AgencyDatabaseProvider;
import tech.zerofiltre.freeland.infra.providers.database.agency.mapper.AgencyJPAMapperImpl;
import tech.zerofiltre.freeland.infra.providers.database.client.ClientDatabaseProvider;
import tech.zerofiltre.freeland.infra.providers.database.client.mapper.ClientJPAMapperImpl;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.FreelancerDatabaseProvider;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.mapper.FreelancerJPAMapperImpl;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.ServiceContractDatabaseProvider;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.WagePortageAgreementDatabaseProvider;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.ServiceContractJPAMapperImpl;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.WagePortageAgreementJPAMapperImpl;

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
class StartServiceContractIT {

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
  StartServiceContract startServiceContract;
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
    startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
        serviceContractProvider, serviceContractNotificationProvider);

  }

  @Test
  @DisplayName("Start service contract must return a proper service contract containing all needed info")
  void executeStart_mustProduceAProperServiceContract() throws StartServiceContractException {

    //arrange : A wage portage agreement is required for any service contract signature
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

    //A client data is required, registered or not
    client.setClientId(clientId);
    client.setAddress(clientAddress);
    client.setDescription(CLIENT_DESCRIPTION);
    client.setPhoneNumber(PHONE_NUMBER);
    clientProvider.registerClient(client);

    doNothing().when(serviceContractNotificationProvider).notify(any());

    //when
    serviceContract = startServiceContract
        .execute(wagePortageAgreement.getWagePortageAgreementId(), client.getClientId(), SERVICE_CONTRACT_TERMS, rate);

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
    assertThat(currentWagePortageAgreement.getEndDate()).isEqualTo(serviceContract.getEndDate());

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