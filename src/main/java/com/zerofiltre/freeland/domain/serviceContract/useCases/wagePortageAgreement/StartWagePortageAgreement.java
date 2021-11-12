package com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;

public class StartWagePortageAgreement {


  public WagePortageAgreement execute(Agency agency, Freelancer freelancer) {
    WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
    return wagePortageAgreement;

  }

}
