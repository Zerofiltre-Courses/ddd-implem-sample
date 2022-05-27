package tech.zerofiltre.freeland.domain.servicecontract.model;


public class ServiceContractId {

  private final Long contractNumber;


  public ServiceContractId(Long contractNumber) {
    this.contractNumber = contractNumber;
  }

  public long getContractNumber() {
    return contractNumber;
  }
}
