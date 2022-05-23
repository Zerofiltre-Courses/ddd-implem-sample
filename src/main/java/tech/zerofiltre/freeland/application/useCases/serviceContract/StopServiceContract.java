package tech.zerofiltre.freeland.application.useCases.serviceContract;

import tech.zerofiltre.freeland.domain.serviceContract.model.ServiceContract;
import java.util.Optional;

public class StopServiceContract {

  private final ServiceContractProvider serviceContractProvider;

  public StopServiceContract(ServiceContractProvider serviceContractProvider) {

    this.serviceContractProvider = serviceContractProvider;
  }

  public void execute(ServiceContract serviceContract) throws StopServiceContractException {
    Optional<ServiceContract> optionalRegisteredServiceContract = serviceContractProvider
        .serviceContractOfId(serviceContract.getServiceContractId());

    optionalRegisteredServiceContract.ifPresent(
        registeredServiceContract -> {
          serviceContractProvider.removeServiceContract(registeredServiceContract.getServiceContractId());
        });

    optionalRegisteredServiceContract.orElseThrow(
        () -> new StopServiceContractException(
            "The service contract " + serviceContract.getServiceContractId().getContractNumber()
                + " might have already been deleted")
    );

  }

}
