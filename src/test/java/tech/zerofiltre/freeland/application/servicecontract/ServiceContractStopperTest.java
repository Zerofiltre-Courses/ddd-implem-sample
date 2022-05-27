package tech.zerofiltre.freeland.application.servicecontract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ServiceContractStopperTest {

    ServiceContractStopper serviceContractStopper;
    WagePortageAgreement wagePortageAgreement;
    WagePortageAgreementId wagePortageAgreementId = new WagePortageAgreementId(12L);
    ServiceContractId serviceContractId = new ServiceContractId(13L);
    ServiceContract serviceContract;


    @Mock
    ServiceContractProvider serviceContractProvider;
    @Mock
    ServiceContractNotificationProvider serviceContractNotificationProvider;
    @Mock
    WagePortageAgreementProvider wagePortageAgreementProvider;


    @BeforeEach
    void init() {
        serviceContractStopper = new ServiceContractStopper();
        serviceContract = ServiceContract.builder()
                .serviceContractProvider(serviceContractProvider)
                .serviceContractNotificationProvider(serviceContractNotificationProvider)
                .serviceContractId(serviceContractId)
                .build();
        wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .wagePortageAgreementId(wagePortageAgreementId)
                .build();
    }

    @Test
    @DisplayName("Stop service contract must delete the service contract ")
    void execute() throws StopServiceContractException {

        //ARRANGE
        when(serviceContractProvider.serviceContractOfId(any())).thenReturn(Optional.of(serviceContract));
        doNothing().when(serviceContractProvider).removeServiceContract(any());

        //ACT
        serviceContractStopper.execute(serviceContract);

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
                .isThrownBy(() -> serviceContractStopper.execute(serviceContract));
        verify(serviceContractProvider, times(0)).removeServiceContract(serviceContractId);

    }
}