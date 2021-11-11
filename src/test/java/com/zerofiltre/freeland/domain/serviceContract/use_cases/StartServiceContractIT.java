package com.zerofiltre.freeland.domain.serviceContract.use_cases;

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
import java.util.Date;
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
  Address address = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
  Address address1 = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");
  Address address2 = new Address("3", "Metz", "75012", "Rue du Cathédrale", "France");


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
  @DisplayName("Start service contract must return a proper service contract")
  void executeStart_mustProduceAProperServiceContract() throws ServiceContractException {

    //arrange
    Date wagePortageStartDate = new Date();
    wagePortageAgreement.setStartDate(wagePortageStartDate);
    wagePortageAgreement.setServiceFeesRate(0.05f);
    wagePortageAgreement.setAgencyId(agencyId);
    wagePortageAgreement.setFreelancerId(freelancerId);
    wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);
    wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(null));

    agency.setAgencyId(agencyId);
    agency.setAddress(address);
    agency.setDescription(AGENCY_DESCRIPTION);
    agency.setPhoneNumber(PHONE_NUMBER);
    agency = agencyProvider.registerAgency(agency);

    freelancer.setFreelancerId(freelancerId);
    freelancer.setAddress(address1);
    freelancer.setDescription(FREELANCER_DESCRIPTION);
    freelancer.setPhoneNumber(PHONE_NUMBER);
    freelancer = freelancerProvider.registerFreelancer(freelancer);

    wagePortageAgreement.setFreelancerId(freelancer.getFreelancerId());
    wagePortageAgreement.setAgencyId(agency.getAgencyId());
    wagePortageAgreement = wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
    assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotNull();

    client.setClientId(clientId);
    client.setAddress(address2);
    client.setDescription(CLIENT_DESCRIPTION);
    client.setPhoneNumber(PHONE_NUMBER);

    //when
    serviceContract = startServiceContract
        .execute(wagePortageAgreement.getWagePortageAgreementId(), client, SERVICE_CONTRACT_TERMS, rate);

    //then
    assertThat(serviceContract).isNotNull();

    ServiceContractId contractId = serviceContract.getServiceContractId();
    assertThat(contractId).isNotNull();
    assertThat(contractId.getContractNumber()).isNotNull();

    WagePortageAgreement currentWagePortageAgreement = serviceContract.getWagePortageAgreement();
    assertThat(currentWagePortageAgreement.getWagePortageAgreementId().getAgreementNumber())
        .isEqualTo(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber());

    assertThat(currentWagePortageAgreement.getStartDate()).isBeforeOrEqualTo(serviceContract.getStartDate());
    assertThat(currentWagePortageAgreement.getEndDate()).isEqualTo(serviceContract.getEndDate());

    ClientId currentClientId = serviceContract.getClientId();
    assertThat(currentClientId).isNotNull();
    assertThat(currentClientId.getSiren()).isEqualTo(clientId.getSiren());
    assertThat(currentClientId.getName()).isEqualTo(clientId.getName());

    assertThat(serviceContract.getServiceContractId()).isNotNull();
    assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

    assertThat(serviceContract.getTerms()).isNotEmpty();
    assertThat(serviceContract.getRate()).isNotNull();
    assertThat(serviceContract.getRate().getPrice()).isGreaterThan(0);
    assertThat(serviceContract.getRate().getFrequency()).isNotNull();
    assertThat(serviceContract.getRate().getCurrency()).isNotNull();

  }


}