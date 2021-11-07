package com.zerofiltre.freeland.domain.serviceContract.use_cases;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;

public interface ServiceContractProvider {

  ServiceContract serviceContractFromId(ServiceContractId serviceContractId);

  ServiceContract registerContract(ServiceContract serviceContract);


}
