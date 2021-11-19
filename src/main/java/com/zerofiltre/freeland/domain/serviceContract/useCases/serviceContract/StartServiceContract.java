package com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractStarted;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.WagePortageAgreementProvider;
import java.util.Date;

public class StartServiceContract {

  private final ClientProvider clientProvider;
  private final WagePortageAgreementProvider wagePortageAgreementProvider;
  private final ServiceContractProvider serviceContractProvider;
  private final ServiceContractNotifier serviceContractNotifier;

  public StartServiceContract(ClientProvider clientProvider,
      WagePortageAgreementProvider wagePortageAgreementProvider,
      ServiceContractProvider serviceContractProvider,
      ServiceContractNotifier serviceContractNotifier) {
    this.clientProvider = clientProvider;
    this.wagePortageAgreementProvider = wagePortageAgreementProvider;
    this.serviceContractProvider = serviceContractProvider;
    this.serviceContractNotifier = serviceContractNotifier;
  }

  public ServiceContract execute(WagePortageAgreementId wagePortageAgreementId, Client client, String terms, Rate rate)
      throws StartServiceContractException {
    ServiceContract serviceContract = new ServiceContract();

    WagePortageAgreement wagePortageAgreement = nonNullWagePortageAgreement(wagePortageAgreementId);
    serviceContract.setWagePortageAgreement(wagePortageAgreement);

    client = getRegisteredClient(client);

    serviceContract.setServiceContractId(new ServiceContractId(null));
    serviceContract.setClientId(client.getClientId());
    serviceContract.setRate(rate);
    serviceContract.setTerms(terms);
    serviceContract.setStartDate(new Date());

    serviceContract = serviceContractProvider.registerContract(serviceContract);
    ServiceContractEvent serviceContractStarted = new ServiceContractStarted(
        serviceContract.getClientId(),
        wagePortageAgreement.getFreelancerId(),
        wagePortageAgreement.getAgencyId(),
        rate,
        wagePortageAgreement.getServiceFeesRate(),
        serviceContract.getStartDate()
    );
    serviceContractNotifier.notify(serviceContractStarted);
    return serviceContract;
  }

  private Client getRegisteredClient(Client client) {
    return clientProvider.clientOfId(client.getClientId()).orElseGet(() -> clientProvider.registerClient(client));
  }


  private WagePortageAgreement nonNullWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId)
      throws StartServiceContractException {
    return wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreementId)
        .orElseThrow(() -> new StartServiceContractException(
            "There is no wage portage agreement available for" + wagePortageAgreementId.getAgreementNumber())
        );
  }

}
