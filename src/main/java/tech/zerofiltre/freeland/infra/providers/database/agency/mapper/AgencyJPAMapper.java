package tech.zerofiltre.freeland.infra.providers.database.agency.mapper;

import org.mapstruct.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.model.*;

@Mapper(componentModel = "spring")
public abstract class AgencyJPAMapper {


    @Mapping(target = "siren", expression = "java(agency.getAgencyId()!=null?agency.getAgencyId().getSiren():null)")
    @Mapping(target = "name", expression = "java(agency.getAgencyId()!=null?agency.getAgencyId().getName():null)")
    @Mapping(target = "streetNumber", expression = "java(agency.getAddress().getStreetNumber())")
    @Mapping(target = "streetName", expression = "java(agency.getAddress().getStreetName())")
    @Mapping(target = "city", expression = "java(agency.getAddress().getCity())")
    @Mapping(target = "postalCode", expression = "java(agency.getAddress().getPostalCode())")
    @Mapping(target = "country", expression = "java(agency.getAddress().getCountry())")
    public abstract AgencyJPA toJPA(Agency agency);


    public abstract Agency fromJPA(AgencyJPA agencyJPA);


    @AfterMapping
    Agency completeMapping(@MappingTarget Agency.AgencyBuilder result, AgencyJPA agencyJPA) {
        AgencyId agencyId = new AgencyId(agencyJPA.getSiren(), agencyJPA.getName());
        result.agencyId(agencyId);
        Address address = new Address(agencyJPA.getStreetNumber(), agencyJPA.getStreetName(), agencyJPA.getPostalCode(),
                agencyJPA.getCity(), agencyJPA.getCountry());
        result.address(address);
        return result.build();
    }


}
