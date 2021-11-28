package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import java.util.Date;
import tech.zerofiltre.freeland.domain.Rate;
import tech.zerofiltre.freeland.domain.client.ClientProvider;
import tech.zerofiltre.freeland.domain.client.model.Client;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractStarted;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.WagePortageAgreementProvider;

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
