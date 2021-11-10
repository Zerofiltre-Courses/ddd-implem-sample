package com.zerofiltre.freeland.infra.providers.database.freelancer.mapper;

import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.infra.providers.database.freelancer.FreelancerJPARepository;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FreelancerJPAMapper {

  @Autowired
  private FreelancerJPARepository freelancerJPARepository;

  @Mapping(target = "siren", expression = "java(freelancer.getFreelancerId().getSiren())")
  @Mapping(target = "name", expression = "java(freelancer.getFreelancerId().getName())")
  public abstract FreelancerJPA toJPA(Freelancer freelancer);

  public abstract Freelancer fromJPA(FreelancerJPA FreelancerJPA);


  @AfterMapping
  Freelancer addFreelancerId(@MappingTarget Freelancer result, FreelancerJPA freelancerJPA) {
    FreelancerId FreelancerId = new FreelancerId(freelancerJPA.getSiren(), freelancerJPA.getName());
    result.setFreelancerId(FreelancerId);
    return result;
  }

  @AfterMapping
  FreelancerJPA addId(@MappingTarget FreelancerJPA result, Freelancer freelancer) {
    if (freelancer != null) {
      FreelancerId freelancerId = freelancer.getFreelancerId();
      if (freelancerId != null && freelancerId.getSiren() != null) {
        return freelancerJPARepository.findBySiren(freelancerId.getSiren())
            .map(freelancerJPA -> {
              result.setId(freelancerJPA.getId());
              return result;
            }).orElse(result);
      }
    }
    return result;
  }

}
