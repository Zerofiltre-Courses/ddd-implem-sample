package com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.zerofiltre.freeland.domain.agency.AgencyProvider;
import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StartWagePortageAgreementTest {

  public static final String AGENCY_SIREN = "agency_siren";
  public static final String AGENCY_NAME = "agency_name";
  public static final String FREELANCER_SIREN = "freelancer_siren";
  public static final String FREELANCER_NAME = "freelancer_name";
  public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
  public static final float SERVICE_FEES_RATE = 0.05f;
  public static final long AGREEMENT_NUMBER = 12L;

  StartWagePortageAgreement startWagePortageAgreement;
  Agency agency = new Agency();
  Freelancer freelancer = new Freelancer();
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
    startWagePortageAgreement = new StartWagePortageAgreement(wagePortageAgreementProvider, agencyProvider,
        freelancerProvider);
    agency.setAgencyId(agencyId);
    freelancer.setFreelancerId(freelancerId);
  }


  @Test
  @DisplayName("start a Wage portage Agreement must produce a proper agreement")
  void execute_mustReturnAProperAgreement() throws StartWagePortageAgreementException {

    //ARRANGE
    when(freelancerProvider.freelancerOfId(any())).thenReturn(Optional.of(freelancer));
    when(agencyProvider.agencyOfId(any())).thenReturn(Optional.of(agency));
    when(wagePortageAgreementProvider.registerWagePortageAgreement(any())).thenAnswer(invocationOnMock -> {
      WagePortageAgreement wagePortageAgreement = invocationOnMock.getArgument(0);
      wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(AGREEMENT_NUMBER));
      return wagePortageAgreement;
    });

    //ACT
    WagePortageAgreement wagePortageAgreement = startWagePortageAgreement
        .execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE);

    //ASSERT
    assertThat(wagePortageAgreement).isNotNull();
    assertThat(wagePortageAgreement.getStartDate()).isBeforeOrEqualTo(new Date());
    assertThat(wagePortageAgreement.getTerms()).isNotEmpty();
    assertThat(wagePortageAgreement.getServiceFeesRate()).isGreaterThan(0);

    WagePortageAgreementId wagePortageAgreementId = wagePortageAgreement.getWagePortageAgreementId();
    assertThat(wagePortageAgreementId).isNotNull();
    assertThat(wagePortageAgreementId.getAgreementNumber()).isNotNull();
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
            () -> startWagePortageAgreement.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
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
            () -> startWagePortageAgreement.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
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
            () -> startWagePortageAgreement.execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE));
  }
}