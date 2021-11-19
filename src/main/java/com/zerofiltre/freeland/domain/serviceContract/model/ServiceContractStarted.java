package com.zerofiltre.freeland.domain.serviceContract.model;

import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.client.model.ClientId;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import java.util.Date;

public class ServiceContractStarted extends ServiceContractEvent {

  private final ClientId clientId;
  private final FreelancerId freelancerId;
  private final AgencyId agencyId;
  private final Rate rate;
  private final float serviceFeesRate;
  private final Date startDate;


  public ServiceContractStarted(ClientId clientId,
      FreelancerId freelancerId, AgencyId agencyId, Rate rate, float serviceFeesRate, Date startDate) {
    this.clientId = clientId;
    this.freelancerId = freelancerId;
    this.agencyId = agencyId;
    this.rate = rate;
    this.serviceFeesRate = serviceFeesRate;
    this.startDate = startDate;
  }

  public ClientId getClientId() {
    return clientId;
  }

  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  public AgencyId getAgencyId() {
    return agencyId;
  }

  public Rate getRate() {
    return rate;
  }

  public float getServiceFeesRate() {
    return serviceFeesRate;
  }

  public Date getStartDate() {
    return startDate;
  }

}
