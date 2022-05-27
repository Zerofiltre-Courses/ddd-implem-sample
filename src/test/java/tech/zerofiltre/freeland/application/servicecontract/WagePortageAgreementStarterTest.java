package tech.zerofiltre.freeland.application.servicecontract;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.*;
import tech.zerofiltre.freeland.application.servicecontract.*;
import tech.zerofiltre.freeland.domain.agency.*;
import tech.zerofiltre.freeland.domain.agency.model.*;
import tech.zerofiltre.freeland.domain.freelancer.*;
import tech.zerofiltre.freeland.domain.freelancer.model.*;
import tech.zerofiltre.freeland.domain.servicecontract.*;
import tech.zerofiltre.freeland.domain.servicecontract.model.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WagePortageAgreementStarterTest {

    public static final String AGENCY_SIREN = "agency_siren";
    public static final String AGENCY_NAME = "agency_name";
    public static final String FREELANCER_SIREN = "freelancer_siren";
    public static final String FREELANCER_NAME = "freelancer_name";
    public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
    public static final float SERVICE_FEES_RATE = 0.05f;
    public static final long AGREEMENT_NUMBER = 12L;

    WagePortageAgreementStarter wagePortageAgreementStarter;
    Agency agency;
    Freelancer freelancer;
    AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
    FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);

    @Mock
    FreelancerProvider freelancerProvider;
    @Mock
    AgencyProvider agencyProvider;
    @Mock
    WagePortageAgreementProvider wagePortageAgreementProvider;


    @BeforeEach
    void setUp() {
        wagePortageAgreementStarter = new WagePortageAgreementStarter(wagePortageAgreementProvider, freelancerProvider, agencyProvider);
        Agency.builder().agencyProvider(agencyProvider).agencyId(agencyId).build();
        freelancer = Freelancer.builder().freelancerProvider(freelancerProvider).freelancerId(freelancerId).build();
        agency = Agency.builder().agencyProvider(agencyProvider).build();
    }

    @Test
    @DisplayName("start a Wage portage Agreement must produce a proper agreement")
    void execute_mustReturnAProperAgreement() throws StartWagePortageAgreementException {

        //ARRANGE
        when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.of(freelancer));
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(agency));
        when(wagePortageAgreementProvider.registerWagePortageAgreement(any())).thenAnswer(invocationOnMock -> {
            return WagePortageAgreement.builder()
                    .copy(invocationOnMock.getArgument(0))
                    .wagePortageAgreementId(new WagePortageAgreementId(AGREEMENT_NUMBER)).build();
        });

        //ACT
        WagePortageAgreement wagePortageAgreement = wagePortageAgreementStarter
                .execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE);

        //ASSERT
        assertThat(wagePortageAgreement).isNotNull();
        assertThat(wagePortageAgreement.getStartDate()).isBeforeOrEqualTo(new Date());
        assertThat(wagePortageAgreement.getTerms()).isNotEmpty();
        assertThat(wagePortageAgreement.getServiceFeesRate()).isPositive();

        WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
        assertThat(wagePortageAgreementId).isNotNull();
        assertThat(wagePortageAgreementId.getAgreementNumber()).isNotZero();
        assertThat(wagePortageAgreementId.getAgreementNumber()).isEqualTo(AGREEMENT_NUMBER);

        AgencyId registeredAgencyId = wagePortageAgreement.getAgencyId();
        assertThat(registeredAgencyId).isNotNull();
        assertThat(registeredAgencyId.getSiren()).isEqualTo(agencyId.getSiren());
        assertThat(registeredAgencyId.getName()).isEqualTo(agencyId.getName());

        FreelancerId registeredFreelancerId = wagePortageAgreement.getFreelancerId();
        assertThat(registeredFreelancerId).isNotNull();
        assertThat(registeredFreelancerId.getSiren()).isEqualTo(freelancerId.getSiren());
        assertThat(registeredFreelancerId.getName()).isEqualTo(freelancerId.getName());

    }

    @Test
    @DisplayName("Start a wage portage agreement throws a StartWagePortageAgreementException if the freelancer is not registered")
    void execute_throwsStartWagePortageAgreementException_onMissingFreelancer() {

        //ARRANGE
        when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.empty());
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.ofNullable(agency));

        //ACT & ASSERT
        assertThatExceptionOfType(StartWagePortageAgreementException.class)
                .isThrownBy(
                        () -> wagePortageAgreementStarter.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
    }

    @Test
    @DisplayName("Start a wage portage agreement throws a StartWagePortageAgreementException if the Agency is not registered")
    void execute_throwsStartWagePortageAgreementException_onMissingAgency() {

        //ARRANGE
        when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.ofNullable(freelancer));
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatExceptionOfType(StartWagePortageAgreementException.class)
                .isThrownBy(
                        () -> wagePortageAgreementStarter.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
    }

    @Test
    @DisplayName("Start a wage portage agreement throws a StartWagePortageAgreementException if the Agency and the freelancer are not registered")
    void execute_throwsStartWagePortageAgreementException_onMissingAgencyAndFreelance() {

        //ARRANGE
        when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.empty());
        when(agencyProvider.agencyOfId(any())).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatExceptionOfType(StartWagePortageAgreementException.class)
                .isThrownBy(
                        () -> wagePortageAgreementStarter.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
    }
}