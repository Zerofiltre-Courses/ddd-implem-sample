package com.zerofiltre.freeland.infra.providers.database.agency.mapper;

import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AgencyJPAMapper {


  @Mapping(target = "siren", expression = "java(agency.getAgencyId().getSiren())")
  @Mapping(target = "name", expression = "java(agency.getAgencyId().getName())")
  public abstract AgencyJPA toJPA(Agency agency);

  public abstract Agency fromJPA(AgencyJPA agencyJPA);


  @AfterMapping
  Agency addAgencyId(@MappingTarget Agency result, AgencyJPA agencyJPA) {
    AgencyId agencyId = new AgencyId(agencyJPA.getSiren(), agencyJPA.getName());
    result.setAgencyId(agencyId);
    return result;
  }


}
