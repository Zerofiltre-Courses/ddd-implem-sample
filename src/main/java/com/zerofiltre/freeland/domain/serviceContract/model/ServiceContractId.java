package com.zerofiltre.freeland.domain.serviceContract.model;


public class ServiceContractId {

  private final String contractNumber;


  public ServiceContractId(String contractNumber) {
    this.contractNumber = contractNumber;
  }

  public String getContractNumber() {
    return contractNumber;
  }
}
