package tech.zerofiltre.freeland.application.servicecontract;

import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

public class ServiceContractStopper {

    public void execute(ServiceContract serviceContract) throws StopServiceContractException {
        Optional<ServiceContract> registeredServiceContract = serviceContract.of(serviceContract.getServiceContractId());
        registeredServiceContract.ifPresent(ServiceContract::remove);
        registeredServiceContract.orElseThrow(() -> new StopServiceContractException(
                "The service contract " + serviceContract.getServiceContractId().getContractNumber() + " might have already been deleted"
        ));
    }

}
