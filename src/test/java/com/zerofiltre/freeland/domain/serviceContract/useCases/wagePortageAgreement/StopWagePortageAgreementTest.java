package com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StopWagePortageAgreementTest {

  public static final long AGREEMENT_NUMBER = 12L;
  StopWagePortageAgreement stopWagePortageAgreement;

  WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
  WagePortageAgreementId wagePortageAgreementId = new WagePortageAgreementId(AGREEMENT_NUMBER);

  @Mock
  WagePortageAgreementProvider wagePortageAgreementProvider;

  @BeforeEach
  void init() {
    wagePortageAgreement.setWagePortageAgreementId(wagePortageAgreementId);
    stopWagePortageAgreement = new StopWagePortageAgreement(wagePortageAgreementProvider);
  }

  @Test
  @DisplayName("Stop agreement make sure the agreement exists then deletes it")
  void execute() throws StopWagePortageAgreementException {
    //ARRANGE
    when(wagePortageAgreementProvider.wagePortageAgreementOfId(any()))
        .thenAnswer(invocationOnMock -> Optional.of(wagePortageAgreement));
    doNothing().when(wagePortageAgreementProvider).removeWagePortageAgreement(any());

    //ACT
    stopWagePortageAgreement.execute(wagePortageAgreement);

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
        .isThrownBy(() -> stopWagePortageAgreement.execute(wagePortageAgreement));

    WagePortageAgreementId usedWagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
    verify(wagePortageAgreementProvider, times(1))
        .wagePortageAgreementOfId(usedWagePortageAgreementId);

    verify(wagePortageAgreementProvider, times(0))
        .removeWagePortageAgreement(usedWagePortageAgreementId);
  }
}