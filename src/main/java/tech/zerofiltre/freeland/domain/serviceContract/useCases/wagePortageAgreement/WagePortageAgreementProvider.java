package tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import java.util.Optional;

public interface WagePortageAgreementProvider {

  Optional<WagePortageAgreement> wagePortageAgreementOfId(WagePortageAgreementId wagePortageAgreementId);

  WagePortageAgreement registerWagePortageAgreement(WagePortageAgreement wagePortageAgreement);

  void removeWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId);

}
