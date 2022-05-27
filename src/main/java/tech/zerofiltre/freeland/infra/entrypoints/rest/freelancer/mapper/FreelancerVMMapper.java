package tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.mapper;

import org.mapstruct.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.infra.entrypoints.rest.freelancer.model.*;

@Mapper(componentModel = "spring")
public abstract class FreelancerVMMapper {

    @Mapping(target = "siren", expression = "java(freelancer.getFreelancerId().getSiren())")
    @Mapping(target = "name", expression = "java(freelancer.getFreelancerId().getName())")
    @Mapping(target = "streetNumber", expression = "java(freelancer.getAddress().getStreetNumber())")
    @Mapping(target = "streetName", expression = "java(freelancer.getAddress().getStreetName())")
    @Mapping(target = "city", expression = "java(freelancer.getAddress().getCity())")
    @Mapping(target = "postalCode", expression = "java(freelancer.getAddress().getPostalCode())")
    @Mapping(target = "country", expression = "java(freelancer.getAddress().getCountry())")
    public abstract FreelancerVM toVM(Freelancer freelancer);

    public abstract Freelancer fromVM(FreelancerVM freelancerVM);

    @AfterMapping
    Freelancer completeMapping(@MappingTarget Freelancer.FreelancerBuilder result, FreelancerVM freelancerVM) {
        FreelancerId freelancerId = new FreelancerId(freelancerVM.getSiren(), freelancerVM.getName());
        result.freelancerId(freelancerId);
        Address address = new Address(freelancerVM.getStreetNumber(), freelancerVM.getStreetName(), freelancerVM.getPostalCode(),
                freelancerVM.getCity(), freelancerVM.getCountry());
        result.address(address);
        return result.build();
    }

}
