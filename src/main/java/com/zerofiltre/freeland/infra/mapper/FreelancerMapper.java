package com.zerofiltre.freeland.infra.mapper;

import com.zerofiltre.freeland.domain.entities.Freelancer;
import com.zerofiltre.freeland.infra.dto.FreelancerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FreelancerMapper {


  public abstract FreelancerDTO toDTO(Freelancer freelancer);

  public abstract Freelancer toEntity(Freelancer freelancer);

}
