package com.zerofiltre.freeland.infra.providers.database.serviceContract.mapper;

import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import com.zerofiltre.freeland.infra.providers.database.agency.AgencyJPARepository;
import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import com.zerofiltre.freeland.infra.providers.database.freelancer.FreelancerJPARepository;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import com.zerofiltre.freeland.infra.providers.database.serviceContract.model.WagePortageAgreementJPA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

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

  @Mapping(target = "agreementNumber", expression = "java(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber())")
  @Mapping(target = "freelancer", source = "freelancerId")
  @Mapping(target = "agency", source = "agencyId")
  public abstract WagePortageAgreementJPA toJPA(WagePortageAgreement wagePortageAgreement);


  WagePortageAgreementId toWagePortageAgreementId(Long agreementNumber) {
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
    return agencyJPARepository.findById(agencyId.getSiren()).orElse(null);
  }

  FreelancerJPA toFreelancerJPA(FreelancerId freelancerId) {
    if (freelancerId == null) {
      return null;
    }
    return freelancerJPARepository.findById(freelancerId.getSiren()).orElse(null);
  }
}
