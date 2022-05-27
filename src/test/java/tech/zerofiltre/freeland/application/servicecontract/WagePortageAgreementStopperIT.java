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
class WagePortageAgreementStopperIT {

    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String PHONE_NUMBER = "0658425369";
    public static final String FREELANCER_DESCRIPTION = "Zerofiltre freelancer";
    public static final String AGENCY_DESCRIPTION = "Procmo Agency";
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    Freelancer freelancer;
    Agency agency;
    WagePortageAgreement wagePortageAgreement;
    Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
    Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");

    WagePortageAgreementStopper wagePortageAgreementStopper;

    @Autowired
    WagePortageAgreementProvider wagePortageAgreementProvider;
    @Autowired
    AgencyProvider agencyProvider;
    @Autowired
    FreelancerProvider freelancerProvider;

    @BeforeAll
    void init() {
        wagePortageAgreementStopper = new WagePortageAgreementStopper();
        Agency.builder().agencyProvider(agencyProvider).build();
        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider).build();
        agency = Agency.builder().agencyProvider(agencyProvider).build();
        wagePortageAgreement = WagePortageAgreement.builder().wagePortageAgreementProvider(wagePortageAgreementProvider).build();

    }

    @Test
    @DisplayName("After stopping wage portage agreement, it must not be retrievable")
    void execute() throws StopWagePortageAgreementException {

        //ARRANGE
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

        wagePortageAgreement =WagePortageAgreement.builder()
                .wagePortageAgreementProvider(wagePortageAgreementProvider)
                .freelancerId(freelancer.getFreelancerId())
                .agencyId(agency.getAgencyId())
                .build();
        wagePortageAgreement = wagePortageAgreement.register();
        assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotZero();

        //ACT
        wagePortageAgreementStopper.stop(wagePortageAgreement);

        //ASSERT
        assertThat(wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreement.getWagePortageAgreementId()))
                .isEmpty();

    }

}
