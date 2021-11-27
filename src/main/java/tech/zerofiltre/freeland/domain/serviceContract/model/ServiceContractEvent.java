package tech.zerofiltre.freeland.domain.serviceContract.model;

import java.util.Date;
import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.Frequency;

public class ServiceContractEvent {

  private long serviceContractNumber;
  private String clientName;
  private String clientSiren;
  private String freelancerName;
  private String freelancerSiren;
  private String agencyName;
  private String agencySiren;
  private float rateValue;
  private Frequency rateFrequency;
  private Currency rateCurrency;
  private float serviceFeesRate;
  private Date startDate;

  public ServiceContractEvent() {
  }

  public ServiceContractEvent(long serviceContractNumber, String clientName, String clientSiren,
      String freelancerName, String freelancerSiren, String agencyName, String agencySiren, float rateValue,
      Frequency rateFrequency, Currency rateCurrency, float serviceFeesRate, Date startDate) {
    this.serviceContractNumber = serviceContractNumber;
    this.clientName = clientName;
    this.clientSiren = clientSiren;
    this.freelancerName = freelancerName;
    this.freelancerSiren = freelancerSiren;
    this.agencyName = agencyName;
    this.agencySiren = agencySiren;
    this.rateValue = rateValue;
    this.rateFrequency = rateFrequency;
    this.rateCurrency = rateCurrency;
    this.serviceFeesRate = serviceFeesRate;
    this.startDate = startDate;
  }

  public long getServiceContractNumber() {
    return serviceContractNumber;
  }

  public void setServiceContractNumber(long serviceContractNumber) {
    this.serviceContractNumber = serviceContractNumber;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientSiren() {
    return clientSiren;
  }

  public void setClientSiren(String clientSiren) {
    this.clientSiren = clientSiren;
  }

  public String getFreelancerName() {
    return freelancerName;
  }

  public void setFreelancerName(String freelancerName) {
    this.freelancerName = freelancerName;
  }

  public String getFreelancerSiren() {
    return freelancerSiren;
  }

  public void setFreelancerSiren(String freelancerSiren) {
    this.freelancerSiren = freelancerSiren;
  }

  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  public String getAgencySiren() {
    return agencySiren;
  }

  public void setAgencySiren(String agencySiren) {
    this.agencySiren = agencySiren;
  }

  public float getRateValue() {
    return rateValue;
  }

  public void setRateValue(float rateValue) {
    this.rateValue = rateValue;
  }

  public Frequency getRateFrequency() {
    return rateFrequency;
  }

  public void setRateFrequency(Frequency rateFrequency) {
    this.rateFrequency = rateFrequency;
  }

  public Currency getRateCurrency() {
    return rateCurrency;
  }

  public void setRateCurrency(Currency rateCurrency) {
    this.rateCurrency = rateCurrency;
  }

  public float getServiceFeesRate() {
    return serviceFeesRate;
  }

  public void setServiceFeesRate(float serviceFeesRate) {
    this.serviceFeesRate = serviceFeesRate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
