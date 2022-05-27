package tech.zerofiltre.freeland.application.servicecontract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.*;
import tech.zerofiltre.freeland.application.servicecontract.*;
import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.*;
import tech.zerofiltre.freeland.infra.providers.database.agency.mapper.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.*;
import tech.zerofiltre.freeland.infra.providers.database.freelancer.mapper.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.*;
import tech.zerofiltre.freeland.infra.providers.database.serviceContract.mapper.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import({
        WagePortageAgreementDatabaseProvider.class,
        WagePortageAgreementJPAMapperImpl.class,
        FreelancerDatabaseProvider.class,
        FreelancerJPAMapperImpl.class,
        AgencyDatabaseProvider.class,
        AgencyJPAMapperImpl.class})
class WagePortageAgreementStarterIT {

    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
    public static final float SERVICE_FEES_RATE = 0.05f;
    public static final String AGENCY_DESCRIPTION = "Procmo Agency";
    public static final String PHONE_NUMBER = "0658425369";
    public static final String FREELANCER_DESCRIPTION = "Zerofiltre freelancer";


    Freelancer freelancer;
    Agency agency;
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
    Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");

    WagePortageAgreementStarter wagePortageAgreementStarter;

    @Autowired
    private WagePortageAgreementProvider wagePortageAgreementProvider;
    @Autowired
    private FreelancerProvider freelancerProvider;
    @Autowired
    private AgencyProvider agencyProvider;


    @BeforeAll
    void init() {
        wagePortageAgreementStarter = new WagePortageAgreementStarter(wagePortageAgreementProvider, freelancerProvider, agencyProvider);
        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider).build();
        agency = Agency.builder().agencyProvider(agencyProvider).build();

    }


    @Test
    void startWagePortageAgreement_mustProduceAProperWagePortageAgreement() throws StartWagePortageAgreementException {
        //ARRANGE :An agency and a freelancer are required for any wage portage agreement signature

        agency = Agency.builder().agencyProvider(agencyProvider)
                .agencyId(agencyId)
                .address(agencyAddress)
                .description(AGENCY_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        agency = agencyProvider.registerAgency(agency);

        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider)
                .freelancerId(freelancerId)
                .address(freelancerAddress)
                .description(FREELANCER_DESCRIPTION)
                .phoneNumber(PHONE_NUMBER)
                .build();
        freelancer = freelancerProvider.registerFreelancer(freelancer);

        //ACT
        WagePortageAgreement registeredWagePortageAgreement = wagePortageAgreementStarter
                .execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE);

        //ASSERT
        assertThat(registeredWagePortageAgreement).isNotNull();
        assertThat(registeredWagePortageAgreement.getWagePortageAgreementId()).isNotNull();
        assertThat(registeredWagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotZero();

        assertThat(registeredWagePortageAgreement.getServiceFeesRate()).isPositive();
        assertThat(registeredWagePortageAgreement.getStartDate()).isBeforeOrEqualTo(new Date());
        assertThat(registeredWagePortageAgreement.getTerms()).isEqualTo(WAGE_PORTAGE_TERMS);

        AgencyId registeredAgencyId = registeredWagePortageAgreement.getAgencyId();
        assertThat(registeredAgencyId.getName()).isEqualTo(agencyId.getName());
        assertThat(registeredAgencyId.getSiren()).isEqualTo(agencyId.getSiren());

        Optional<Agency> registeredAgency = agencyProvider.agencyOfId(registeredAgencyId);
        assertThat(registeredAgency).isNotEmpty();
        registeredAgency.ifPresent(theRegisteredAgency -> {
            assertThat(theRegisteredAgency.getAddress().getCity()).isEqualTo(agencyAddress.getCity());
            assertThat(theRegisteredAgency.getAddress().getStreetName()).isEqualTo(agencyAddress.getStreetName());
            assertThat(theRegisteredAgency.getAddress().getStreetNumber()).isEqualTo(agencyAddress.getStreetNumber());
            assertThat(theRegisteredAgency.getAddress().getPostalCode()).isEqualTo(agencyAddress.getPostalCode());
            assertThat(theRegisteredAgency.getAddress().getCountry()).isEqualTo(agencyAddress.getCountry());
        });

        FreelancerId registeredFreelancerId = registeredWagePortageAgreement.getFreelancerId();
        assertThat(registeredFreelancerId.getName()).isEqualTo(freelancerId.getName());
        assertThat(registeredFreelancerId.getSiren()).isEqualTo(freelancerId.getSiren());

        Optional<Freelancer> registeredFreelancer = freelancerProvider.freelancerOfId(registeredFreelancerId);
        assertThat(registeredFreelancer).isNotEmpty();
        registeredFreelancer.ifPresent(theRegisteredFreelancer -> {
            assertThat(theRegisteredFreelancer.getAddress().getCity()).isEqualTo(freelancerAddress.getCity());
            assertThat(theRegisteredFreelancer.getAddress().getStreetName()).isEqualTo(freelancerAddress.getStreetName());
            assertThat(theRegisteredFreelancer.getAddress().getStreetNumber()).isEqualTo(freelancerAddress.getStreetNumber());
            assertThat(theRegisteredFreelancer.getAddress().getPostalCode()).isEqualTo(freelancerAddress.getPostalCode());
            assertThat(theRegisteredFreelancer.getAddress().getCountry()).isEqualTo(freelancerAddress.getCountry());
        });

    }

}
