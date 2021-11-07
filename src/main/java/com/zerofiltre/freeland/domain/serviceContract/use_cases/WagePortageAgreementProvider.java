package com.zerofiltre.freeland.domain.serviceContract.use_cases;

import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;

public interface WagePortageAgreementProvider {

  WagePortageAgreement wagePortageAgreementOfId(WagePortageAgreementId wagePortageAgreementId);

  WagePortageAgreement registerWagePortageAgreement(WagePortageAgreement wagePortageAgreement);

}
