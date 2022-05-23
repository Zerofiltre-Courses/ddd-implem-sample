package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract;

import org.springframework.web.bind.annotation.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.application.useCases.wagePortageAgreement.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

import javax.validation.*;

@RestController
@RequestMapping("wage-portage-agreement")
public class WagePortageAgreementController {

    private final WagePortageAgreementVMMapper mapper;
    private final StartWagePortageAgreement startWagePortageAgreement;
    private final WagePortageAgreementProvider wagePortageAgreementProvider;
    private final AgencyProvider agencyProvider;
    private final FreelancerProvider freelancerProvider;

    public WagePortageAgreementController(WagePortageAgreementVMMapper mapper, WagePortageAgreementProvider wagePortageAgreementProvider, AgencyProvider agencyProvider, FreelancerProvider freelancerProvider) {
        this.mapper = mapper;
        this.wagePortageAgreementProvider = wagePortageAgreementProvider;
        this.agencyProvider = agencyProvider;
        this.freelancerProvider = freelancerProvider;
        this.startWagePortageAgreement = new StartWagePortageAgreement(wagePortageAgreementProvider, agencyProvider, freelancerProvider);

    }


    @PostMapping("/start")
    public WagePortageAgreementVM registerAgreement(@RequestBody @Valid WagePortageAgreementVM wagePortageAgreementVM) throws StartWagePortageAgreementException {
        WagePortageAgreement wagePortageAgreement = mapper.fromVM(wagePortageAgreementVM);

        WagePortageAgreement startedWagePortageAgreement = startWagePortageAgreement.execute(
                wagePortageAgreement.getAgencyId(),
                wagePortageAgreement.getFreelancerId(),
                wagePortageAgreement.getTerms(),
                wagePortageAgreement.getServiceFeesRate()
        );
        return mapper.toVM(startedWagePortageAgreement);
    }
}
