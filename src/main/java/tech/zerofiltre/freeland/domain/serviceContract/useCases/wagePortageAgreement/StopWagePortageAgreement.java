package tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.Optional;

public class StopWagePortageAgreement {

  private final WagePortageAgreementProvider wagePortageAgreementProvider;

  public StopWagePortageAgreement(
      WagePortageAgreementProvider wagePortageAgreementProvider) {
    this.wagePortageAgreementProvider = wagePortageAgreementProvider;
  }

  public void execute(WagePortageAgreement wagePortageAgreement) throws StopWagePortageAgreementException {
    WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
    Optional<WagePortageAgreement> optionalRegisteredWagePortageAgreement = wagePortageAgreementProvider
        .wagePortageAgreementOfId(wagePortageAgreementId);

    optionalRegisteredWagePortageAgreement.ifPresent(registeredWagePortageAgreement -> {
      wagePortageAgreementProvider
          .removeWagePortageAgreement(registeredWagePortageAgreement.getWagePortageAgreementId());
    });
    optionalRegisteredWagePortageAgreement.orElseThrow(() -> new StopWagePortageAgreementException(
        "The agreement " + wagePortageAgreementId.getAgreementNumber() + " might have already been deleted"));
  }

}
