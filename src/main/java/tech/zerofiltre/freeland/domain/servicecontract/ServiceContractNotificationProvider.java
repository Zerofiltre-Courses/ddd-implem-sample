package tech.zerofiltre.freeland.domain.servicecontract;

import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContractEvent;

public interface ServiceContractNotificationProvider {

  void notify(ServiceContractEvent serviceContractEvent);

}
