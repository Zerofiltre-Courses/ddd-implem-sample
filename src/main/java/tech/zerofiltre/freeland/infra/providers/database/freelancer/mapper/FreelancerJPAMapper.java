package tech.zerofiltre.freeland.infra.providers.database.freelancer.mapper;

import tech.zerofiltre.freeland.domain.Address;
import tech.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import tech.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class FreelancerJPAMapper {

  @Mapping(target = "siren", expression = "java(freelancer.getFreelancerId().getSiren())")
  @Mapping(target = "name", expression = "java(freelancer.getFreelancerId().getName())")
  @Mapping(target = "streetNumber", expression = "java(freelancer.getAddress().getStreetNumber())")
  @Mapping(target = "streetName", expression = "java(freelancer.getAddress().getStreetName())")
  @Mapping(target = "city", expression = "java(freelancer.getAddress().getCity())")
  @Mapping(target = "postalCode", expression = "java(freelancer.getAddress().getPostalCode())")
  @Mapping(target = "country", expression = "java(freelancer.getAddress().getCountry())")
  public abstract FreelancerJPA toJPA(Freelancer freelancer);

  public abstract Freelancer fromJPA(FreelancerJPA FreelancerJPA);

  @AfterMapping
  Freelancer completeMapping(@MappingTarget Freelancer result, FreelancerJPA freelancerJPA) {
    FreelancerId FreelancerId = new FreelancerId(freelancerJPA.getSiren(), freelancerJPA.getName());
    result.setFreelancerId(FreelancerId);
    Address address = new Address(freelancerJPA.getStreetNumber(), freelancerJPA.getStreetName(), freelancerJPA.getPostalCode(),
        freelancerJPA.getCity(), freelancerJPA.getCountry());
    result.setAddress(address);
    return result;
  }

}
