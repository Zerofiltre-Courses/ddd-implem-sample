package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.WagePortageAgreementProvider;

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
  ServiceContractNotifier serviceContractNotifier;

  @Autowired
  AgencyProvider agencyProvider;

  @Autowired
  FreelancerProvider freelancerProvider;
  @Autowired
  ClientProvider clientProvider;

  @BeforeAll
  void init() {
    startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
        serviceContractProvider, serviceContractNotifier);
    stopServiceContract = new StopServiceContract(serviceContractProvider);
  }

  @Test
  @DisplayName("StopServiceContract must delete the service contract and the related wage portage agreement ")
  void execute() throws StartServiceContractException, StopServiceContractException {

    //ARRANGE : Save a wage portage agreement and a service contract
    createAServiceContract();
    doNothing().when(serviceContractNotifier).notify(any());

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
    wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(null));

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

    serviceContract = startServiceContract
        .execute(wagePortageAgreement.getWagePortageAgreementId(), client, SERVICE_CONTRACT_TERMS, rate);
    assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotNull();

  }

}
