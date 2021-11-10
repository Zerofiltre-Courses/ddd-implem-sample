package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.infra.providers.database.agency.AgencyJPARepository;
import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import com.zerofiltre.freeland.infra.providers.database.freelancer.FreelancerJPARepository;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.WagePortageAgreementJPARepository;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class WagePortageAgreementJPAMapper {

  @Autowired
  private AgencyJPARepository agencyJPARepository;
  @Autowired
  private FreelancerJPARepository freelancerJPARepository;
  @Autowired
  private WagePortageAgreementJPARepository wagePortageAgreementJPARepository;


  @Mapping(target = "wagePortageAgreementId", source = "agreementNumber")
  @Mapping(target = "freelancerId", source = "freelancer")
  @Mapping(target = "agencyId", source = "agency")
  public abstract WagePortageAgreement fromJPA(WagePortageAgreementJPA wagePortageAgreementJPA);

  @Mapping(target = "agreementNumber", expression = "java(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber())")
  @Mapping(target = "freelancer", source = "freelancerId")
  @Mapping(target = "agency", source = "agencyId")
  public abstract WagePortageAgreementJPA toJPA(WagePortageAgreement wagePortageAgreement);

  @AfterMapping
  WagePortageAgreementJPA addId(@MappingTarget WagePortageAgreementJPA result,
      WagePortageAgreement wagePortageAgreement) {
    if (wagePortageAgreement != null) {
      WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
      if (wagePortageAgreementId.getAgreementNumber() != null) {
        return wagePortageAgreementJPARepository.findByAgreementNumber(wagePortageAgreementId.getAgreementNumber())
            .map(wagePortageAgreementJPA -> {
              result.setId(wagePortageAgreementJPA.getId());
              return result;
            }).orElse(result);
      }
    }
    return result;
  }


  WagePortageAgreementId toWagePortageAgreementId(String agreementNumber) {
    if (agreementNumber == null) {
      return null;
    }
    return new WagePortageAgreementId(agreementNumber);
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
    return agencyJPARepository.findBySiren(agencyId.getSiren()).orElse(null);
  }

  FreelancerJPA toFreelancerJPA(FreelancerId freelancerId) {
    if (freelancerId == null) {
      return null;
    }
    return freelancerJPARepository.findBySiren(freelancerId.getSiren()).orElse(null);
  }
}
