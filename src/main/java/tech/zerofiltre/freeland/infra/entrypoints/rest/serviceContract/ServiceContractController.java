package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.application.servicecontract.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

import javax.validation.*;

@RestController
@RequestMapping("service-contract")
public class ServiceContractController {

    private final ServiceContractStarter serviceContractStarter;
    private final ServiceContractVMMapper mapper;


    public ServiceContractController(ServiceContractVMMapper mapper, ClientProvider clientProvider, WagePortageAgreementProvider wagePortageAgreementProvider, ServiceContractProvider serviceContractProvider, ServiceContractNotificationProvider notificationProvider) {
        this.mapper = mapper;
        this.serviceContractStarter = new ServiceContractStarter(
                clientProvider, wagePortageAgreementProvider, serviceContractProvider, notificationProvider);

    }


    @PostMapping("/start")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ServiceContractVM startServiceContract(@RequestBody @Valid ServiceContractVM serviceContractVM) throws StartServiceContractException {
        ServiceContract serviceContract = mapper.fromVM(serviceContractVM);
        ServiceContract startedServiceContract = serviceContractStarter.start(
                serviceContract.getWagePortageAgreement().getWagePortageAgreementId(),
                serviceContract.getClientId(),
                serviceContract.getWagePortageAgreement().getTerms(),
                serviceContract.getRate()
        );
        return mapper.toVM(startedServiceContract);
    }
}
