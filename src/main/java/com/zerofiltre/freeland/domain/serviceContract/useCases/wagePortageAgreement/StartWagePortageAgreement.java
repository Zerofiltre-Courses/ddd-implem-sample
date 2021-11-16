package com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import com.zerofiltre.freeland.domain.agency.AgencyProvider;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import java.util.Date;

public class StartWagePortageAgreement {

  private final WagePortageAgreementProvider wagePortageAgreementProvider;
  private final AgencyProvider agencyProvider;
  private final FreelancerProvider freelancerProvider;

  public StartWagePortageAgreement(
      WagePortageAgreementProvider wagePortageAgreementProvider,
      AgencyProvider agencyProvider, FreelancerProvider freelancerProvider) {
    this.wagePortageAgreementProvider = wagePortageAgreementProvider;
    this.agencyProvider = agencyProvider;
    this.freelancerProvider = freelancerProvider;
  }

  public WagePortageAgreement execute(AgencyId agencyId, FreelancerId freelancerId, String terms, float serviceFeesRate)
      throws StartWagePortageAgreementException {

    agencyProvider.agencyOfId(agencyId)
        .orElseThrow(() -> new StartWagePortageAgreementException("There is no agency for: " + agencyId));

    freelancerProvider.freelancerOfId(freelancerId)
        .orElseThrow(
            () -> new StartWagePortageAgreementException("There is no freelancer for: " + agencyId));

    WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
    wagePortageAgreement.setAgencyId(agencyId);
    wagePortageAgreement.setFreelancerId(freelancerId);
    wagePortageAgreement.setTerms(terms);
    wagePortageAgreement.setServiceFeesRate(serviceFeesRate);
    wagePortageAgreement.setStartDate(new Date());
    return wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
  }

}
