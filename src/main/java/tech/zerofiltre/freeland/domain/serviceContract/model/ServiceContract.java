package tech.zerofiltre.freeland.domain.serviceContract.model;

import tech.zerofiltre.freeland.application.useCases.serviceContract.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.model.*;

import java.util.*;

public class ServiceContract {

    private final ServiceContractProvider serviceContractProvider;
    private ServiceContractId serviceContractId;
    private WagePortageAgreement wagePortageAgreement;
    private ClientId clientId;
    private Rate rate;
    private String terms;
    private Date startDate;
    private Date endDate;

    public ServiceContract(ServiceContractProvider serviceContractProvider) {
        this.serviceContractProvider = serviceContractProvider;
    }

    public ServiceContract init(WagePortageAgreement wagePortageAgreement, ClientId clientId, String terms, Rate rate, Date startDate, Date endDate) {
        this.wagePortageAgreement = wagePortageAgreement;
        this.terms = terms;
        this.rate = rate;
        this.clientId = clientId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.serviceContractId = serviceContractProvider.registerContract(this).getServiceContractId();
        return this;
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
}
