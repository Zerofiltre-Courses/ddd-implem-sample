package tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.mapper;

import org.mapstruct.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.serviceContract.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.serviceContract.model.*;

@Mapper(componentModel = "spring")
public abstract class WagePortageAgreementVMMapper {

    @Mapping(target = "wagePortageAgreementId", source = "agreementNumber")
    @Mapping(target = "freelancerId", source = "freelancerSiren")
    @Mapping(target = "agencyId", source = "agencySiren")
    public abstract WagePortageAgreement fromVM(WagePortageAgreementVM wagePortageAgreementVM);

    @Mapping(target = "agreementNumber", source = "wagePortageAgreementId")
    @Mapping(target = "freelancerSiren", source = "freelancerId")
    @Mapping(target = "agencySiren", source = "agencyId")
    public abstract WagePortageAgreementVM toVM(WagePortageAgreement wagePortageAgreement);


    WagePortageAgreementId toWagePortageAgreementId(long agreementNumber) {
        return new WagePortageAgreementId(agreementNumber);
    }

    Long toAgreementNumber(WagePortageAgreementId wagePortageAgreementId) {
        if (wagePortageAgreementId == null) {
            return null;
        }
        return wagePortageAgreementId.getAgreementNumber();
    }

    FreelancerId toFreelancerId(String freelancerSiren) {
        if (freelancerSiren == null) {
            return null;
        }
        return new FreelancerId(freelancerSiren, null);
    }

    String toFreelancerSiren(FreelancerId freelancerId) {
        if (freelancerId == null) {
            return null;
        }
        return freelancerId.getSiren();
    }

    AgencyId toAgencyId(String agencySiren) {
        if (agencySiren == null) {
            return null;
        }
        return new AgencyId(agencySiren, null);
    }

    String toAgencySiren(AgencyId agencyId) {
        if (agencyId == null) {
            return null;
        }
        return agencyId.getSiren();
    }


}
