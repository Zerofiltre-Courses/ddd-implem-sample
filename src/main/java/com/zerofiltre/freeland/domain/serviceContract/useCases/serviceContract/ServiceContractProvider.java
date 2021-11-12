package com.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract;

import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import com.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import java.util.Optional;

public interface ServiceContractProvider {

  Optional<ServiceContract> serviceContractFromId(ServiceContractId serviceContractId);

  ServiceContract registerContract(ServiceContract serviceContract);


}
