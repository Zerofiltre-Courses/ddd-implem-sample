package tech.zerofiltre.freeland.application.servicecontract;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.client.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

public class ServiceContractStarter {

    private final ServiceContractProvider serviceContractProvider;
    private final ServiceContractNotificationProvider serviceContractNotificationProvider;
    ServiceContract serviceContract;
    WagePortageAgreement wagePortageAgreement;
    Client client;

    public ServiceContractStarter(ClientProvider clientProvider,
                                  WagePortageAgreementProvider wagePortageAgreementProvider,
                                  ServiceContractProvider serviceContractProvider,
                                  ServiceContractNotificationProvider serviceContractNotificationProvider) {
        this.serviceContractProvider = serviceContractProvider;
        this.serviceContractNotificationProvider = serviceContractNotificationProvider;

        wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .build();
        client = Client.builder()
                .clientProvider(clientProvider)
                .build();
    }

    public ServiceContract start(WagePortageAgreementId wagePortageAgreementId, ClientId clientId, String terms, Rate rate)
            throws StartServiceContractException {
        wagePortageAgreement = nonNullWagePortageAgreement(wagePortageAgreementId);
        checkClient(clientId);
        serviceContract = ServiceContract.builder()
                .serviceContractProvider(serviceContractProvider)
                .serviceContractNotificationProvider(serviceContractNotificationProvider)
                .wagePortageAgreement(wagePortageAgreement)
                .clientId(clientId)
                .terms(terms)
                .rate(rate)
                .starDate(null)
                .endDate(null)
                .build()
                .start();
        serviceContract.notifyContractStarted();
        return serviceContract;
    }

    private void checkClient(ClientId clientId) throws StartServiceContractException {
        if (client.of(clientId).isEmpty())
            throw new StartServiceContractException("There is no client available for" + clientId.getSiren());
    }


    private WagePortageAgreement nonNullWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId)
            throws StartServiceContractException {
        return wagePortageAgreement.of(wagePortageAgreementId)
                .orElseThrow(() -> new StartServiceContractException(
                        "There is no wage portage agreement available for" + wagePortageAgreementId.getAgreementNumber())
                );
    }

}
