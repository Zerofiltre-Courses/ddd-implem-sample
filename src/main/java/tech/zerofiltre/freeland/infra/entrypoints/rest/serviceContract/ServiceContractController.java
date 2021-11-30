package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract;

import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.client.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.serviceContract.*;
import tech.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

import javax.validation.*;

@RestController
@RequestMapping("service-contract")
public class ServiceContractController {

    private final StartServiceContract startServiceContract;
    private final ServiceContractVMMapper mapper;
    private final ClientProvider clientProvider;
    private final WagePortageAgreementProvider wagePortageAgreementProvider;
    private final ServiceContractProvider serviceContractProvider;
    private final ServiceContractNotificationProvider notificationProvider;


    public ServiceContractController(ServiceContractVMMapper mapper, ClientProvider clientProvider, WagePortageAgreementProvider wagePortageAgreementProvider, ServiceContractProvider serviceContractProvider, ServiceContractNotificationProvider notificationProvider) {
        this.mapper = mapper;
        this.clientProvider = clientProvider;
        this.wagePortageAgreementProvider = wagePortageAgreementProvider;
        this.serviceContractProvider = serviceContractProvider;
        this.notificationProvider = notificationProvider;

        this.startServiceContract = new StartServiceContract(
                clientProvider, wagePortageAgreementProvider, serviceContractProvider, notificationProvider);

    }


    @PostMapping("/start")
    public ServiceContractVM startServiceContract(@RequestBody @Valid ServiceContractVM serviceContractVM) throws StartServiceContractException {
        ServiceContract serviceContract = mapper.fromVM(serviceContractVM);
        ServiceContract startedServiceContract = startServiceContract.execute(
                serviceContract.getWagePortageAgreement().getWagePortageAgreementId(),
                serviceContract.getClientId(),
                serviceContract.getWagePortageAgreement().getTerms(),
                serviceContract.getRate()
        );
        return mapper.toVM(startedServiceContract);
    }
}
