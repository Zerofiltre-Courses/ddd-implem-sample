package com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractEvent;

public interface ServiceContractNotifier {

  void notify(ServiceContractEvent serviceContractEvent);

}
