package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;

public interface ServiceContractNotificationProvider {

  void notify(ServiceContractEvent serviceContractEvent);

}
