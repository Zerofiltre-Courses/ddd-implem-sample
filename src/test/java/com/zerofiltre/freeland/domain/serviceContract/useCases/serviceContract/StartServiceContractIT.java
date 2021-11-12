package com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerofiltre.freeland.domain.Address;
import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.Rate.Currency;
import com.zerofiltre.freeland.domain.Rate.Frequency;
import com.zerofiltre.freeland.domain.agency.AgencyProvider;
import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.ServiceContractException;
import com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.ServiceContractProvider;
import com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.StartServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.WagePortageAgreementProvider;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
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
  Address agencyAdress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
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


  @BeforeAll
  void setUp() {
    startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
        serviceContractProvider);

  }

  @Test
  @DisplayName("Start service contract must return a proper service contract containing all needed info")
  void executeStart_mustProduceAProperServiceContract() throws ServiceContractException {

    //arrange : A wage portage agreement is required for any service contract signature
    Date wagePortageStartDate = new Date();
    wagePortageAgreement.setStartDate(wagePortageStartDate);
    wagePortageAgreement.setServiceFeesRate(0.05f);
    wagePortageAgreement.setAgencyId(agencyId);
    wagePortageAgreement.setFreelancerId(freelancerId);
    wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);
    wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(null));

    agency.setAgencyId(agencyId);
    agency.setAddress(agencyAdress);
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

    //when
    serviceContract = startServiceContract
        .execute(wagePortageAgreement.getWagePortageAgreementId(), client, SERVICE_CONTRACT_TERMS, rate);

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
    assertThat(currentClientId.getSiren()).isEqualTo(clientId.getSiren());
    assertThat(currentClientId.getName()).isEqualTo(clientId.getName());

    //the attached registered has been created properly
    Optional<Client> registeredClient = clientProvider.clientOfId(currentClientId);
    assertThat(registeredClient).isNotEmpty();
    registeredClient.ifPresent(theRegisteredClient -> {
      assertThat(theRegisteredClient.getAddress().getCity()).isEqualTo(clientAddress.getCity());
      assertThat(theRegisteredClient.getAddress().getStreetName()).isEqualTo(clientAddress.getStreetName());
      assertThat(theRegisteredClient.getAddress().getStreetNumber()).isEqualTo(clientAddress.getStreetNumber());
      assertThat(theRegisteredClient.getAddress().getPostalCode()).isEqualTo(clientAddress.getPostalCode());
      assertThat(theRegisteredClient.getAddress().getCountry()).isEqualTo(clientAddress.getCountry());
    });

    //the service contract has all  the needed info
    assertThat(serviceContract.getServiceContractId()).isNotNull();
    assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

    assertThat(serviceContract.getTerms()).isNotEmpty();
    assertThat(serviceContract.getRate()).isNotNull();
    assertThat(serviceContract.getRate().getPrice()).isGreaterThan(0);
    assertThat(serviceContract.getRate().getFrequency()).isNotNull();
    assertThat(serviceContract.getRate().getCurrency()).isNotNull();

    //the service contract is attached to the agency attached to the Wage portage agreement
    AgencyId registeredAgencyId = serviceContract.getWagePortageAgreement().getAgencyId();
    assertThat(registeredAgencyId.getName()).isEqualTo(agencyId.getName());
    assertThat(registeredAgencyId.getSiren()).isEqualTo(agencyId.getSiren());

    Optional<Agency> registeredAgency = agencyProvider.agencyOfId(registeredAgencyId);
    assertThat(registeredAgency).isNotEmpty();
    registeredAgency.ifPresent(theRegisteredAgency -> {
      assertThat(theRegisteredAgency.getAddress().getCity()).isEqualTo(agencyAdress.getCity());
      assertThat(theRegisteredAgency.getAddress().getStreetName()).isEqualTo(agencyAdress.getStreetName());
      assertThat(theRegisteredAgency.getAddress().getStreetNumber()).isEqualTo(agencyAdress.getStreetNumber());
      assertThat(theRegisteredAgency.getAddress().getPostalCode()).isEqualTo(agencyAdress.getPostalCode());
      assertThat(theRegisteredAgency.getAddress().getCountry()).isEqualTo(agencyAdress.getCountry());
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