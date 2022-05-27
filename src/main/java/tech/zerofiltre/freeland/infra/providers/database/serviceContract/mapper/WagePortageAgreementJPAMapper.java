package tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.model.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.model.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.model.*;

@Mapper(componentModel = "spring")
public abstract class WagePortageAgreementJPAMapper {

    @Autowired
    private AgencyJPARepository agencyJPARepository;
    @Autowired
    private FreelancerJPARepository freelancerJPARepository;


    @Mapping(target = "wagePortageAgreementId", source = "agreementNumber")
    @Mapping(target = "freelancerId", source = "freelancer")
    @Mapping(target = "agencyId", source = "agency")
    public abstract WagePortageAgreement fromJPA(WagePortageAgreementJPA wagePortageAgreementJPA);

    @Mapping(target = "agreementNumber", source = "wagePortageAgreementId")
    @Mapping(target = "freelancer", source = "freelancerId")
    @Mapping(target = "agency", source = "agencyId")
    public abstract WagePortageAgreementJPA toJPA(WagePortageAgreement wagePortageAgreement);


    WagePortageAgreementId toWagePortageAgreementId(Long agreementNumber) {
        if (agreementNumber == null) {
            return null;
        }
        return new WagePortageAgreementId(agreementNumber);
    }

    Long toAgreementNumber(WagePortageAgreementId wagePortageAgreementId) {
        if (wagePortageAgreementId == null) {
            return null;
        }
        return wagePortageAgreementId.getAgreementNumber();
    }

    FreelancerId toFreelancerId(FreelancerJPA freelancerJPA) {
        if (freelancerJPA == null) {
            return null;
        }
        return new FreelancerId(freelancerJPA.getSiren(), freelancerJPA.getName());
    }

    AgencyId toAgencyId(AgencyJPA agencyJPA) {
        if (agencyJPA == null) {
            return null;
        }
        return new AgencyId(agencyJPA.getSiren(), agencyJPA.getName());
    }

    AgencyJPA toAgencyJPA(AgencyId agencyId) {
        if (agencyId == null) {
            return null;
        }
        return agencyJPARepository.findById(agencyId.getSiren()).orElse(null);
    }

    FreelancerJPA toFreelancerJPA(FreelancerId freelancerId) {
        if (freelancerId == null) {
            return null;
        }
        return freelancerJPARepository.findById(freelancerId.getSiren()).orElse(null);
    }
}
