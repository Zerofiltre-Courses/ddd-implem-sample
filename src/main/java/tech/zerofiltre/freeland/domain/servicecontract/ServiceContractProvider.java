package tech.zerofiltre.freeland.domain.servicecontract;

import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContractId;
import tech.zerofiltre.freeland.domain.servicecontract.model.ServiceContract;
import java.util.Optional;

public interface ServiceContractProvider {

  Optional<ServiceContract> serviceContractOfId(ServiceContractId serviceContractId);

  ServiceContract registerContract(ServiceContract serviceContract);

  void removeServiceContract(ServiceContractId serviceContractId);


}
