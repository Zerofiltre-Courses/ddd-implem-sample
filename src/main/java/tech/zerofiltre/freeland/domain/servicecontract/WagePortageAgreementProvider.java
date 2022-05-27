package tech.zerofiltre.freeland.domain.servicecontract;

import tech.zerofiltre.freeland.domain.servicecontract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.domain.servicecontract.model.WagePortageAgreement;
import java.util.Optional;

public interface WagePortageAgreementProvider {

  Optional<WagePortageAgreement> wagePortageAgreementOfId(WagePortageAgreementId wagePortageAgreementId);

  WagePortageAgreement registerWagePortageAgreement(WagePortageAgreement wagePortageAgreement);

  void removeWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId);

}
