package tech.zerofiltre.freeland.application.servicecontract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.application.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WagePortageAgreementStopperTest {

    public static final long AGREEMENT_NUMBER = 12L;
    WagePortageAgreementStopper wagePortageAgreementStopper;

    WagePortageAgreement wagePortageAgreement;
    WagePortageAgreementId wagePortageAgreementId = new WagePortageAgreementId(AGREEMENT_NUMBER);

    @Mock
    WagePortageAgreementProvider wagePortageAgreementProvider;

    @BeforeEach
    void init() {
        wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .wagePortageAgreementId(wagePortageAgreementId).build();
        wagePortageAgreementStopper  = new WagePortageAgreementStopper();
    }

    @Test
    @DisplayName("Stop agreement make sure the agreement exists then deletes it")
    void execute() throws StopWagePortageAgreementException {
        //ARRANGE
        when(wagePortageAgreementProvider.wagePortageAgreementOfId(any()))
                .thenAnswer(invocationOnMock -> Optional.of(wagePortageAgreement));
        doNothing().when(wagePortageAgreementProvider).removeWagePortageAgreement(any());

        //ACT
        wagePortageAgreementStopper.stop(wagePortageAgreement);

        //ASSERT
        WagePortageAgreementId usedWagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
        verify(wagePortageAgreementProvider, times(1))
                .wagePortageAgreementOfId(usedWagePortageAgreementId);

        verify(wagePortageAgreementProvider, times(1))
                .removeWagePortageAgreement(usedWagePortageAgreementId);

    }

    @Test
    @DisplayName("Stop wage portage agreement must throw a stop wage portage exception if the agreement is missing ")
    void stopWagePortageAgreement_mustThrowAnException_onMissingAgreement() throws StopWagePortageAgreementException {
        //ARRANGE
        when(wagePortageAgreementProvider.wagePortageAgreementOfId(any()))
                .thenAnswer(invocationOnMock -> Optional.empty());
        doNothing().when(wagePortageAgreementProvider).removeWagePortageAgreement(any());

        //ASSERT & ACT
        assertThatExceptionOfType(StopWagePortageAgreementException.class)
                .isThrownBy(() -> wagePortageAgreementStopper.stop(wagePortageAgreement));

        WagePortageAgreementId usedWagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
        verify(wagePortageAgreementProvider, times(1))
                .wagePortageAgreementOfId(usedWagePortageAgreementId);

        verify(wagePortageAgreementProvider, times(0))
                .removeWagePortageAgreement(usedWagePortageAgreementId);
    }
}