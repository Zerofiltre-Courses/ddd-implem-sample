package tech.zerofiltre.freeland.infra.providers.database.agency.mapper;

import tech.zerofiltre.freeland.domain.Address;
import tech.zerofiltre.freeland.domain.agency.model.Agency;
import tech.zerofiltre.freeland.domain.agency.model.AgencyId;
import tech.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AgencyJPAMapper {


  @Mapping(target = "siren", expression = "java(agency.getAgencyId().getSiren())")
  @Mapping(target = "name", expression = "java(agency.getAgencyId().getName())")
  @Mapping(target = "streetNumber", expression = "java(agency.getAddress().getStreetNumber())")
  @Mapping(target = "streetName", expression = "java(agency.getAddress().getStreetName())")
  @Mapping(target = "city", expression = "java(agency.getAddress().getCity())")
  @Mapping(target = "postalCode", expression = "java(agency.getAddress().getPostalCode())")
  @Mapping(target = "country", expression = "java(agency.getAddress().getCountry())")
  public abstract AgencyJPA toJPA(Agency agency);


  public abstract Agency fromJPA(AgencyJPA agencyJPA);


  @AfterMapping
  Agency completeMapping(@MappingTarget Agency result, AgencyJPA agencyJPA) {
    AgencyId agencyId = new AgencyId(agencyJPA.getSiren(), agencyJPA.getName());
    result.setAgencyId(agencyId);
    Address address = new Address(agencyJPA.getStreetNumber(), agencyJPA.getStreetName(), agencyJPA.getPostalCode(),
        agencyJPA.getCity(), agencyJPA.getCountry());
    result.setAddress(address);
    return result;
  }


}
