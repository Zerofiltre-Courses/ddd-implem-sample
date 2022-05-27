package tech.zerofiltre.freeland.domain.servicecontract.model;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;

import java.time.*;
import java.util.*;

public class ServiceContract {


    private ServiceContractProvider serviceContractProvider;
    private ServiceContractNotificationProvider serviceContractNotificationProvider;
    private ServiceContractId serviceContractId;
    private WagePortageAgreement wagePortageAgreement;
    private ClientId clientId;
    private Rate rate;
    private String terms;
    private Date startDate;
    private Date endDate;

    private ServiceContract(ServiceContractBuilder serviceContractBuilder) {
        this.serviceContractId = serviceContractBuilder.serviceContractId;
        this.clientId = serviceContractBuilder.clientId;
        this.rate = serviceContractBuilder.rate;
        this.terms = serviceContractBuilder.terms;
        this.startDate = serviceContractBuilder.startDate != null ? serviceContractBuilder.startDate : new Date();
        this.wagePortageAgreement = serviceContractBuilder.wagePortageAgreement;
        this.endDate = serviceContractBuilder.endDate != null ? serviceContractBuilder.endDate : new Date(startDate.getTime() + Duration.ofDays(180).getSeconds() * 1000);

        this.serviceContractProvider = serviceContractBuilder.serviceContractProvider;
        this.serviceContractNotificationProvider = serviceContractBuilder.serviceContractNotificationProvider;
    }

    public static ServiceContractBuilder builder() {
        return new ServiceContract.ServiceContractBuilder();
    }

    public ServiceContract start(){
        serviceContractId = serviceContractProvider.registerContract(this).getServiceContractId();
        return this;
    }

    public void notifyContractStarted() {
        ServiceContractStarted serviceContractStarted = new ServiceContractStarted(
                serviceContractId.getContractNumber(),
                clientId.getName(),
                clientId.getSiren(),
                wagePortageAgreement.getFreelancerId().getName(),
                wagePortageAgreement.getFreelancerId().getSiren(),
                wagePortageAgreement.getAgencyId().getName(),
                wagePortageAgreement.getAgencyId().getSiren(),
                rate.getValue(),
                rate.getFrequency(),
                rate.getCurrency(),
                wagePortageAgreement.getServiceFeesRate(),
                this.getStartDate()
        );
        notify(serviceContractStarted);
    }

    private void notify(ServiceContractEvent event) {
        serviceContractNotificationProvider.notify(event);
    }

    public Optional<ServiceContract> of(ServiceContractId serviceContractId) {
        Optional<ServiceContract> result = serviceContractProvider.serviceContractOfId(serviceContractId);
        result.ifPresent(serviceContract -> {
            serviceContract.serviceContractProvider = this.serviceContractProvider;
            serviceContract.serviceContractNotificationProvider = this.serviceContractNotificationProvider;
        });
        return result;
    }

    public void remove() {
        serviceContractProvider.removeServiceContract(this.getServiceContractId());
    }

    public ServiceContractId getServiceContractId() {
        return serviceContractId;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public Rate getRate() {
        return rate;
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

    public WagePortageAgreement getWagePortageAgreement() {
        return wagePortageAgreement;
    }

    public static class ServiceContractBuilder {
        private ServiceContractProvider serviceContractProvider;
        private ServiceContractNotificationProvider serviceContractNotificationProvider;
        private ServiceContractId serviceContractId;
        private WagePortageAgreement wagePortageAgreement;
        private ClientId clientId;
        private Rate rate;
        private String terms;
        private Date startDate;
        private Date endDate;


        public ServiceContractBuilder copy(ServiceContract serviceContract) {
            this.wagePortageAgreement = serviceContract.wagePortageAgreement;
            this.clientId = serviceContract.clientId;
            this.terms = serviceContract.terms;
            this.rate = serviceContract.rate;
            this.startDate = serviceContract.startDate;
            this.endDate = serviceContract.endDate;
            this.serviceContractId = serviceContract.serviceContractId;
            return this;
        }

        public ServiceContractBuilder serviceContractProvider(ServiceContractProvider serviceContractProvider) {
            this.serviceContractProvider = serviceContractProvider;
            return this;
        }

        public ServiceContractBuilder serviceContractNotificationProvider(ServiceContractNotificationProvider serviceContractNotificationProvider) {
            this.serviceContractNotificationProvider = serviceContractNotificationProvider;
            return this;
        }

        public ServiceContractBuilder serviceContractId(ServiceContractId serviceContractId) {
            this.serviceContractId = serviceContractId;
            return this;
        }

        public ServiceContractBuilder wagePortageAgreement(WagePortageAgreement wagePortageAgreement) {
            this.wagePortageAgreement = wagePortageAgreement;
            return this;
        }

        public ServiceContractBuilder clientId(ClientId clientId) {
            this.clientId = clientId;
            return this;
        }

        public ServiceContractBuilder rate(Rate rate) {
            this.rate = rate;
            return this;
        }

        public ServiceContractBuilder terms(String terms) {
            this.terms = terms;
            return this;
        }

        public ServiceContractBuilder starDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public ServiceContractBuilder endDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public ServiceContract build() {
            return new ServiceContract(this);
        }

    }


}
