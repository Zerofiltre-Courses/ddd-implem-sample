package tech.zerofiltre.freeland.domain.servicecontract.model;

import tech.zerofiltre.freeland.domain.Rate.Currency;
import tech.zerofiltre.freeland.domain.Rate.*;

import java.util.*;

public class ServiceContractEvent {

    private final long serviceContractNumber;
    private final String clientName;
    private final String clientSiren;
    private final String freelancerName;
    private final String freelancerSiren;
    private final String agencyName;
    private final String agencySiren;
    private final float rateValue;
    private final Frequency rateFrequency;
    private final Currency rateCurrency;
    private final float serviceFeesRate;
    private final Date startDate;


    public ServiceContractEvent(long serviceContractNumber, String clientName, String clientSiren, String freelancerName, String freelancerSiren, String agencyName, String agencySiren, float rateValue, Frequency rateFrequency, Currency rateCurrency, float serviceFeesRate, Date startDate) {
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

  public String getClientName() {
        return clientName;
    }


    public String getClientSiren() {
        return clientSiren;
    }


    public String getFreelancerName() {
        return freelancerName;
    }


    public String getFreelancerSiren() {
        return freelancerSiren;
    }


    public String getAgencyName() {
        return agencyName;
    }


    public String getAgencySiren() {
        return agencySiren;
    }


    public float getRateValue() {
        return rateValue;
    }


    public Frequency getRateFrequency() {
        return rateFrequency;
    }


    public Currency getRateCurrency() {
        return rateCurrency;
    }


    public float getServiceFeesRate() {
        return serviceFeesRate;
    }


    public Date getStartDate() {
        return startDate;
    }

}
