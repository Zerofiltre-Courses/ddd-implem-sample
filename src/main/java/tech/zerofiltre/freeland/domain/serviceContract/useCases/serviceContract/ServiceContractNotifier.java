package tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;

public interface ServiceContractNotifier {

  void notify(ServiceContractEvent serviceContractEvent);

}
