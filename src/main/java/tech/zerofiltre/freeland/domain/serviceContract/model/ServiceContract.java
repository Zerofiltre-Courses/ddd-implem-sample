package tech.zerofiltre.freeland.domain.serviceContract.model;

import tech.zerofiltre.freeland.domain.client.model.ClientId;
import tech.zerofiltre.freeland.domain.Rate;
import java.util.Date;

public class ServiceContract {

  private ServiceContractId serviceContractId;
  private WagePortageAgreement wagePortageAgreement;
  private ClientId clientId;
  private Rate rate;
  private String terms;
  private Date startDate;
  private Date endDate;

  public ServiceContract() {
  }

  public ServiceContract(ServiceContractId serviceContractId,
      WagePortageAgreement wagePortageAgreement,
      ClientId clientId, Rate rate, String terms, Date startDate, Date endDate) {
    this.serviceContractId = serviceContractId;
    this.wagePortageAgreement = wagePortageAgreement;
    this.clientId = clientId;
    this.rate = rate;
    this.terms = terms;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public ServiceContractId getServiceContractId() {
    return serviceContractId;
  }

  public void setServiceContractId(ServiceContractId serviceContractId) {
    this.serviceContractId = serviceContractId;
  }

  public ClientId getClientId() {
    return clientId;
  }

  public void setClientId(ClientId clientId) {
    this.clientId = clientId;
  }


  public Rate getRate() {
    return rate;
  }

  public void setRate(Rate rate) {
    this.rate = rate;
  }

  public String getTerms() {
    return terms;
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public WagePortageAgreement getWagePortageAgreement() {
    return wagePortageAgreement;
  }

  public void setWagePortageAgreement(
      WagePortageAgreement wagePortageAgreement) {
    this.wagePortageAgreement = wagePortageAgreement;
  }
}
