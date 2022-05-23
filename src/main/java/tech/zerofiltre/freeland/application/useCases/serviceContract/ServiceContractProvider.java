package tech.zerofiltre.freeland.application.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import java.util.Optional;

public interface ServiceContractProvider {

  Optional<ServiceContract> serviceContractOfId(ServiceContractId serviceContractId);

  ServiceContract registerContract(ServiceContract serviceContract);

  void removeServiceContract(ServiceContractId serviceContractId);


}
