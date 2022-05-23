package tech.zerofiltre.freeland.application.useCases.serviceContract;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.WagePortageAgreementProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StopServiceContractTest {

  StopServiceContract stopServiceContract;
  WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
  WagePortageAgreementId wagePortageAgreementId = new WagePortageAgreementId(12L);
  ServiceContractId serviceContractId = new ServiceContractId(13L);
  ServiceContract serviceContract = new ServiceContract();


  @Mock
  ServiceContractProvider serviceContractProvider;
  @Mock
  WagePortageAgreementProvider wagePortageAgreementProvider;


  @BeforeEach
  void init() {
    stopServiceContract = new StopServiceContract(serviceContractProvider);
    serviceContract.setServiceContractId(serviceContractId);
    wagePortageAgreement.setWagePortageAgreementId(wagePortageAgreementId);
    serviceContract.setWagePortageAgreement(wagePortageAgreement);
  }

  @Test
  @DisplayName("Stop service contract must delete the service contract ")
  void execute() throws StopServiceContractException {

    //ARRANGE
    when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(serviceContract));
    doNothing().when(serviceContractProvider).removeServiceContract(any());

    //ACT
    stopServiceContract.execute(serviceContract);

    //ASSERT
    verify(serviceContractProvider, times(1)).removeServiceContract(serviceContractId);
  }

  @Test
  @DisplayName("Stop service contract must throw StopServiceContractException if the service contract is missing")
  void stopServiceContract_needsAServiceContractToExist() {

    //ARRANGE
    when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.empty());
    when(wagePortageAgreementProvider.wagePortageAgreementOfId(any())).thenReturn(Optional.of(wagePortageAgreement));

    //ACT & ASSERT
    assertThatExceptionOfType(StopServiceContractException.class)
        .isThrownBy(() -> stopServiceContract.execute(serviceContract));
    verify(serviceContractProvider, times(0)).removeServiceContract(serviceContractId);

  }
}