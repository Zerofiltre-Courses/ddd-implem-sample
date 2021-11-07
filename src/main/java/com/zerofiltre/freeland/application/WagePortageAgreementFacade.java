package com.zerofiltre.freeland.application;

import org.springframework.stereotype.Component;

@Component
public interface WagePortageAgreementFacade {

  public void signWagePortageAgreement();

  public void stopWagePortageAgreement();

}
