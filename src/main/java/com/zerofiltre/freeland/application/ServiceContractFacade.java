package com.zerofiltre.freeland.application;

import org.springframework.stereotype.Component;

@Component
public interface ServiceContractFacade {

  public void startServiceContract();

  public void stopServiceContract();


}
