package tech.zerofiltre.freeland.application.servicecontract;

import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

public class WagePortageAgreementStarter {

    private final WagePortageAgreementProvider wagePortageAgreementProvider;
    private final FreelancerProvider freelancerProvider;
    private final AgencyProvider agencyProvider;


    public WagePortageAgreementStarter(WagePortageAgreementProvider wagePortageAgreementProvider, FreelancerProvider freelancerProvider, AgencyProvider agencyProvider) {

        this.wagePortageAgreementProvider = wagePortageAgreementProvider;
        this.agencyProvider = agencyProvider;
        this.freelancerProvider = freelancerProvider;


    }

    public WagePortageAgreement execute(AgencyId agencyId, FreelancerId freelancerId, String terms, float serviceFeesRate)
            throws StartWagePortageAgreementException {


        Optional<Agency> agency = Agency.builder()
                .agencyProvider(agencyProvider)
                .build()
                .of(agencyId);
        if (agency.isEmpty())
            throw new StartWagePortageAgreementException("There is no agency for: " + agencyId);

        Optional<Freelancer> freelancer = Freelancer.builder()
                .freelancerProvider(freelancerProvider)
                .build()
                .of(freelancerId);
        if (freelancer.isEmpty())
            throw new StartWagePortageAgreementException("There is no freelancer for: " + agencyId);

        WagePortageAgreement wagePortageAgreement = WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .freelancerId(freelancerId)
                .agencyId(agencyId)
                .serviceFeesRate(serviceFeesRate)
                .terms(terms)
                .startDate(null)
                .endDate(null)
                .build();

        return wagePortageAgreement.register();
    }

}
