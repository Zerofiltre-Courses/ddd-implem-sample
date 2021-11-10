package com.zerofiltre.freeland.domain.serviceContract.use_cases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.domain.Address;
import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.Rate.Currency;
import com.zerofiltre.freeland.domain.Rate.Frequency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StartServiceContractTest {

  public static final String AGREEMENT_NUMBER = "agreement_number";
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


  @BeforeEach
  void setUp() {
    startServiceContract = new StartServiceContract(clientProvider, wagePortageAgreementProvider,
        serviceContractProvider);

  }

  @Test
  @DisplayName("Start service contract must return a proper service contract")
  void executeStart_mustProduceAProperServiceContract() throws ServiceContractException {

    //given
    wagePortageAgreement.setWagePortageAgreementId(agreementId);
    Date wagePortageStartDate = new Date();
    wagePortageAgreement.setStartDate(wagePortageStartDate);
    wagePortageAgreement.setServiceFeesRate(0.05f);
    wagePortageAgreement.setAgencyId(agencyId);
    wagePortageAgreement.setFreelancerId(freelancerId);
    wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);

    client.setClientId(clientId);
    client.setAddress(address);
    client.setDescription(FREELANCER_DESCRIPTION);
    client.setPhoneNumber(PHONE_NUMBER);

    when(wagePortageAgreementProvider.wagePortageAgreementOfId(agreementId))
        .thenReturn(Optional.of(wagePortageAgreement));
    when(clientProvider.clientOfId(clientId)).thenReturn(Optional.of(client));

    //when
    serviceContract = startServiceContract.execute(agreementId, client, SERVICE_CONTRACT_TERMS, rate);

    //then
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
    assertThat(serviceContract.getServiceContractId().getContractNumber()).isNotEmpty();

    assertThat(serviceContract.getTerms()).isNotEmpty();
    assertThat(serviceContract.getRate()).isNotNull();
    assertThat(serviceContract.getRate().getPrice()).isGreaterThan(0);
    assertThat(serviceContract.getRate().getFrequency()).isNotNull();
    assertThat(serviceContract.getRate().getCurrency()).isNotNull();
    verify(clientProvider, times(1)).clientOfId(any());
    verify(wagePortageAgreementProvider, times(1)).wagePortageAgreementOfId(any());
    verify(serviceContractProvider, times(1)).registerContract(serviceContract);


  }

  @Test
  @DisplayName("Start service contract with non existing agreement throws ServiceContract exception")
  void startServiceContract_mustYieldExceptionIfAgreementDoesNotExist() {

    when(wagePortageAgreementProvider.wagePortageAgreementOfId(agreementId))
        .thenReturn(Optional.empty());

    assertThatExceptionOfType(ServiceContractException.class)
        .isThrownBy(() -> startServiceContract.execute(agreementId, client, SERVICE_CONTRACT_TERMS, rate));
  }

  @Test
  @DisplayName("When the client does not exist, register it")
  void startServiceContract_registerTheClientWhenItDoesNotExist() throws ServiceContractException {

    //given
    wagePortageAgreement.setWagePortageAgreementId(agreementId);

    when(wagePortageAgreementProvider.wagePortageAgreementOfId(agreementId))
        .thenReturn(Optional.of(wagePortageAgreement));

    Client mockCreatedClient = new Client();
    mockCreatedClient.setClientId(clientId);

    when(clientProvider.clientOfId(any())).thenReturn(Optional.empty());
    when(clientProvider.registerClient(any())).thenReturn(mockCreatedClient);

    //when
    serviceContract = startServiceContract.execute(agreementId, client, SERVICE_CONTRACT_TERMS, rate);

    ClientId currentClientId = serviceContract.getClientId();
    assertThat(currentClientId).isNotNull();
    assertThat(currentClientId).isEqualTo(clientId);

    verify(clientProvider, times(1)).clientOfId(any());
    verify(clientProvider, times(1)).registerClient(any());
  }


}