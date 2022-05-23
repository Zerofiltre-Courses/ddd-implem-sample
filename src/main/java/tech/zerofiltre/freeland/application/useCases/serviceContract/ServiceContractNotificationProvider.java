package tech.zerofiltre.freeland.application.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;

public interface ServiceContractNotificationProvider {

  void notify(ServiceContractEvent serviceContractEvent);

}
