package tech.zerofiltre.freeland.application.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.*;

import java.util.*;

public class StartServiceContract {

    private final ClientProvider clientProvider;
    private final WagePortageAgreementProvider wagePortageAgreementProvider;
    private final ServiceContractProvider serviceContractProvider;
    private final ServiceContractNotificationProvider serviceContractNotificationProvider;

    public StartServiceContract(ClientProvider clientProvider,
                                WagePortageAgreementProvider wagePortageAgreementProvider,
                                ServiceContractProvider serviceContractProvider,
                                ServiceContractNotificationProvider serviceContractNotificationProvider) {
        this.clientProvider = clientProvider;
        this.wagePortageAgreementProvider = wagePortageAgreementProvider;
        this.serviceContractProvider = serviceContractProvider;
        this.serviceContractNotificationProvider = serviceContractNotificationProvider;
    }

    public ServiceContract execute(WagePortageAgreementId wagePortageAgreementId, ClientId clientId, String terms, Rate rate)
            throws StartServiceContractException {
        ServiceContract serviceContract = new ServiceContract();

        WagePortageAgreement wagePortageAgreement = nonNullWagePortageAgreement(wagePortageAgreementId);

        serviceContract.setWagePortageAgreement(wagePortageAgreement);

        checkClient(clientId);
        serviceContract.setClientId(clientId);

        serviceContract.setServiceContractId(new ServiceContractId(null));
        serviceContract.setRate(rate);
        serviceContract.setTerms(terms);
        serviceContract.setStartDate(new Date());

        serviceContract = serviceContractProvider.registerContract(serviceContract);
        ServiceContractStarted serviceContractStarted = new ServiceContractStarted(
                serviceContract.getServiceContractId().getContractNumber(),
                serviceContract.getClientId().getName(),
                serviceContract.getClientId().getSiren(),
                wagePortageAgreement.getFreelancerId().getName(),
                wagePortageAgreement.getFreelancerId().getSiren(),
                wagePortageAgreement.getAgencyId().getName(),
                wagePortageAgreement.getAgencyId().getSiren(),
                rate.getValue(),
                rate.getFrequency(),
                rate.getCurrency(),
                wagePortageAgreement.getServiceFeesRate(),
                serviceContract.getStartDate()
        );
        serviceContractNotificationProvider.notify(serviceContractStarted);
        return serviceContract;
    }

    private void checkClient(ClientId clientId) throws StartServiceContractException {
        clientProvider.clientOfId(clientId).orElseThrow(
                () -> new StartServiceContractException(
                        "There is no client available for" + clientId.getSiren())
        );
    }


    private WagePortageAgreement nonNullWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId)
            throws StartServiceContractException {
        return wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreementId)
                .orElseThrow(() -> new StartServiceContractException(
                        "There is no wage portage agreement available for" + wagePortageAgreementId.getAgreementNumber())
                );
    }

}
