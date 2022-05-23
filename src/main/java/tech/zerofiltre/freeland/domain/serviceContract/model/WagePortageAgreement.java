package tech.zerofiltre.freeland.domain.serviceContract.model;

import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import java.util.Date;
import java.util.Objects;


public class WagePortageAgreement {

  private WagePortageAgreementId wagePortageAgreementId;
  private FreelancerId freelancerId;
  private AgencyId agencyId;
  private float serviceFeesRate;
  private String terms;
  private Date startDate;
  private Date endDate;


  public WagePortageAgreement(WagePortageAgreementId wagePortageAgreementId,
      FreelancerId freelancerId, AgencyId agencyId, float serviceFeesRate, String terms, Date startDate, Date endDate) {
    this.wagePortageAgreementId = wagePortageAgreementId;
    this.freelancerId = freelancerId;
    this.agencyId = agencyId;
    this.serviceFeesRate = serviceFeesRate;
    this.terms = terms;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public WagePortageAgreementId getWagePortageAgreementId() {
    return wagePortageAgreementId;
  }

  public void setWagePortageAgreementId(WagePortageAgreementId wagePortageAgreementId) {
    this.wagePortageAgreementId = wagePortageAgreementId;
  }

  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  private void setFreelancerId(FreelancerId freelancerId) {
    this.freelancerId = freelancerId;
  }

  public AgencyId getAgencyId() {
    return agencyId;
  }

  private void setAgencyId(AgencyId agencyId) {
    this.agencyId = agencyId;
  }

  public float getServiceFeesRate() {
    return serviceFeesRate;
  }

  private void setServiceFeesRate(float serviceFeesRate) {
    this.serviceFeesRate = serviceFeesRate;
  }

  public String getTerms() {
    return terms;
  }

  private void setTerms(String terms) {
    this.terms = terms;
  }

  public Date getStartDate() {
    return startDate;
  }

  private void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  private void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public WagePortageAgreement update() {
    return this;
  }

  private void start() {
    this.startDate = new Date();
  }


  private void end() {
    this.endDate = new Date();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WagePortageAgreement that = (WagePortageAgreement) o;
    return Float.compare(that.serviceFeesRate, serviceFeesRate) == 0 && wagePortageAgreementId
        .equals(that.wagePortageAgreementId) && freelancerId.equals(that.freelancerId) && agencyId.equals(that.agencyId)
        && terms.equals(that.terms) && startDate.equals(that.startDate) && endDate.equals(that.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wagePortageAgreementId, freelancerId, agencyId, serviceFeesRate, terms, startDate, endDate);
  }
}
