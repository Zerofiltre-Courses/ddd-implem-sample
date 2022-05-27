package tech.zerofiltre.freeland.domain.servicecontract.model;

import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;

import java.time.*;
import java.util.*;


public class WagePortageAgreement {


    private WagePortageAgreementProvider wagePortageAgreementProvider;
    private WagePortageAgreementId wagePortageAgreementId;
    private FreelancerId freelancerId;
    private AgencyId agencyId;
    private float serviceFeesRate;
    private String terms;
    private Date startDate;
    private Date endDate;


    private WagePortageAgreement(WagePortageAgreementBuilder builder) {
        this.wagePortageAgreementId = builder.wagePortageAgreementId;
        this.freelancerId = builder.freelancerId;
        this.agencyId = builder.agencyId;
        this.serviceFeesRate = builder.serviceFeesRate;
        this.terms = builder.terms;
        this.startDate = builder.startDate != null ? builder.startDate : new Date();
        this.endDate = builder.endDate != null ? builder.endDate : new Date(startDate.getTime() + Duration.ofDays(180).getSeconds() * 1000);
        this.wagePortageAgreementProvider = builder.wagePortageAgreementProvider;
    }

    public static WagePortageAgreementBuilder builder() {
        return new WagePortageAgreement.WagePortageAgreementBuilder();
    }


    private WagePortageAgreement register(FreelancerId freelancerId, AgencyId agencyId, float serviceFeesRate, String terms, Date startDate, Date endDate) {
        this.freelancerId = freelancerId;
        this.agencyId = agencyId;
        this.serviceFeesRate = serviceFeesRate;
        this.terms = terms;
        this.startDate = startDate != null ? startDate : new Date();
        this.endDate = endDate != null ? endDate : new Date(this.startDate.getTime() + Duration.ofDays(180).getSeconds() * 1000);
      return register();
    }
    public WagePortageAgreement register() {
        this.wagePortageAgreementId = wagePortageAgreementProvider.registerWagePortageAgreement(this).getWagePortageAgreementId();
        return this;
    }


    public Optional<WagePortageAgreement> of(WagePortageAgreementId wagePortageAgreementId) {
        Optional<WagePortageAgreement> result = wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreementId);
        result.ifPresent(wagePortageAgreement -> wagePortageAgreement.wagePortageAgreementProvider = this.wagePortageAgreementProvider);
        return result;
    }

    public void remove() {
        wagePortageAgreementProvider.removeWagePortageAgreement(this.getWagePortageAgreementId());
    }

    public WagePortageAgreementId getWagePortageAgreementId() {
        return wagePortageAgreementId;
    }

    public WagePortageAgreementProvider getWagePortageAgreementProvider() {
        return wagePortageAgreementProvider;
    }

    public FreelancerId getFreelancerId() {
        return freelancerId;
    }

    public AgencyId getAgencyId() {
        return agencyId;
    }

    public float getServiceFeesRate() {
        return serviceFeesRate;
    }

    public String getTerms() {
        return terms;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public static class WagePortageAgreementBuilder {
        private WagePortageAgreementProvider wagePortageAgreementProvider;
        private WagePortageAgreementId wagePortageAgreementId;
        private FreelancerId freelancerId;
        private AgencyId agencyId;
        private float serviceFeesRate;
        private String terms;
        private Date startDate;
        private Date endDate;

        public WagePortageAgreementBuilder copy(WagePortageAgreement wagePortageAgreement){
            this.wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
            this.wagePortageAgreementProvider = wagePortageAgreement.getWagePortageAgreementProvider();
            this.freelancerId = wagePortageAgreement.getFreelancerId();
            this.agencyId = wagePortageAgreement.getAgencyId();
            this.serviceFeesRate = wagePortageAgreement.getServiceFeesRate();
            this.terms = wagePortageAgreement.getTerms();
            this.startDate = wagePortageAgreement.getStartDate();
            this.endDate = wagePortageAgreement.getEndDate();
            return this;
        }

        public WagePortageAgreementBuilder wagePortageAgreementProvider(WagePortageAgreementProvider wagePortageAgreementProvider) {
            this.wagePortageAgreementProvider = wagePortageAgreementProvider;
            return this;
        }

        public WagePortageAgreementBuilder freelancerId(FreelancerId freelancerId) {
            this.freelancerId = freelancerId;
            return this;
        }

        public WagePortageAgreementBuilder wagePortageAgreementId(WagePortageAgreementId wagePortageAgreementId) {
            this.wagePortageAgreementId = wagePortageAgreementId;
            return this;
        }

        public WagePortageAgreementBuilder agencyId(AgencyId agencyId) {
            this.agencyId = agencyId;
            return this;
        }

        public WagePortageAgreementBuilder serviceFeesRate(float serviceFeesRate) {
            this.serviceFeesRate = serviceFeesRate;
            return this;
        }

        public WagePortageAgreementBuilder terms(String terms) {
            this.terms = terms;
            return this;
        }

        public WagePortageAgreementBuilder startDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public WagePortageAgreementBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public WagePortageAgreement build() {
            return new WagePortageAgreement(this);
        }
    }
}
