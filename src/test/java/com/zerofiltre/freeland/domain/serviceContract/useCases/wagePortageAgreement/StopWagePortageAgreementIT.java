package com.zerofiltre.freeland.domain.serviceContract.useCases.wagePortageAgreement;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerofiltre.freeland.domain.Address;
import com.zerofiltre.freeland.domain.agency.AgencyProvider;
import com.zerofiltre.freeland.domain.agency.model.Agency;
import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.FreelancerProvider;
import com.zerofiltre.freeland.domain.freelancer.model.Freelancer;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreement;
import com.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class StopWagePortageAgreementIT {

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
  Freelancer freelancer = new Freelancer();
  Agency agency = new Agency();
  WagePortageAgreement wagePortageAgreement = new WagePortageAgreement();
  Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
  Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");

  StopWagePortageAgreement stopWagePortageAgreement;

  @Autowired
  WagePortageAgreementProvider wagePortageAgreementProvider;
  @Autowired
  AgencyProvider agencyProvider;
  @Autowired
  FreelancerProvider freelancerProvider;

  @BeforeAll
  void init() {
    stopWagePortageAgreement = new StopWagePortageAgreement(wagePortageAgreementProvider);

  }

  @Test
  @DisplayName("After stopping wage portage agreement, it must not be retrievable")
  void execute() throws StopWagePortageAgreementException {

    //ARRANGE
    Date wagePortageStartDate = new Date();
    wagePortageAgreement.setStartDate(wagePortageStartDate);
    wagePortageAgreement.setServiceFeesRate(0.05f);
    wagePortageAgreement.setAgencyId(agencyId);
    wagePortageAgreement.setFreelancerId(freelancerId);
    wagePortageAgreement.setTerms(WAGE_PORTAGE_TERMS);
    wagePortageAgreement.setWagePortageAgreementId(new WagePortageAgreementId(null));

    agency.setAgencyId(agencyId);
    agency.setAddress(agencyAddress);
    agency.setDescription(AGENCY_DESCRIPTION);
    agency.setPhoneNumber(PHONE_NUMBER);
    agency = agencyProvider.registerAgency(agency);

    freelancer.setFreelancerId(freelancerId);
    freelancer.setAddress(freelancerAddress);
    freelancer.setDescription(FREELANCER_DESCRIPTION);
    freelancer.setPhoneNumber(PHONE_NUMBER);
    freelancer = freelancerProvider.registerFreelancer(freelancer);

    wagePortageAgreement.setFreelancerId(freelancer.getFreelancerId());
    wagePortageAgreement.setAgencyId(agency.getAgencyId());
    wagePortageAgreement = wagePortageAgreementProvider.registerWagePortageAgreement(wagePortageAgreement);
    assertThat(wagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotNull();

    //ACT
    stopWagePortageAgreement.execute(wagePortageAgreement);

    //ASSERT
    assertThat(wagePortageAgreementProvider.wagePortageAgreementOfId(wagePortageAgreement.getWagePortageAgreementId()))
        .isEmpty();

  }

}
