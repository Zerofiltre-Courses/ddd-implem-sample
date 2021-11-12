package com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import com.zerofiltre.freeland.domain.Rate;
import com.zerofiltre.freeland.domain.client.ClientProvider;
import com.zerofiltre.freeland.domain.client.model.Client;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.WagePortageAgreementProvider;
import java.util.Date;

public class StartServiceContract {

  private final ClientProvider clientProvider;
  private final WagePortageAgreementProvider wagePortageAgreementProvider;
  private final ServiceContractProvider serviceContractProvider;

  public StartServiceContract(ClientProvider clientProvider,
      WagePortageAgreementProvider wagePortageAgreementProvider,
      ServiceContractProvider serviceContractProvider) {
    this.clientProvider = clientProvider;
    this.wagePortageAgreementProvider = wagePortageAgreementProvider;
    this.serviceContractProvider = serviceContractProvider;
  }

  public ServiceContract execute(WagePortageAgreementId wagePortageAgreementId, Client client, String terms, Rate rate)
      throws ServiceContractException {
    ServiceContract serviceContract = new ServiceContract();

    WagePortageAgreement wagePortageAgreement = nonNullWagePortageAgreement(wagePortageAgreementId);
    serviceContract.setWagePortageAgreement(wagePortageAgreement);

    Client registeredClient = getRegisteredClient(client);

    serviceContract.setServiceContractId(new ServiceContractId(null));
    serviceContract.setClientId(registeredClient.getClientId());
    serviceContract.setRate(rate);
    serviceContract.setTerms(terms);
    serviceContract.setStartDate(new Date());

    return serviceContractProvider.registerContract(serviceContract);
  }

  private Client getRegisteredClient(Client client) {
    return clientProvider.clientOfId(client.getClientId()).orElseGet(() -> clientProvider.registerClient(client));
  }


  private WagePortageAgreement nonNullWagePortageAgreement(WagePortageAgreementId wagePortageAgreementId)
      throws ServiceContractException {
    return wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreementId)
        .orElseThrow(() -> new ServiceContractException(
            "There is no wage portage agreement available for" + wagePortageAgreementId.getAgreementNumber())
        );
  }

}
