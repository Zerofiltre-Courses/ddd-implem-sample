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
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class StartWagePortageAgreementIT {

  public static final String FREELANCER_SIREN = "freelancer_siren";
  public static final String FREELANCER_NAME = "freelancer_name";
  public static final String AGENCY_SIREN = "agency_siren";
  public static final String AGENCY_NAME = "agency_name";
  public static final String WAGE_PORTAGE_TERMS = "Wage portage terms";
  public static final float SERVICE_FEES_RATE = 0.05f;
  public static final String AGENCY_DESCRIPTION = "Procmo Agency";
  public static final String PHONE_NUMBER = "0658425369";
  public static final String FREELANCER_DESCRIPTION = "Zerofiltre freelancer";


  Freelancer freelancer = new Freelancer();
  Agency agency = new Agency();
  FreelancerId freelancerId = new FreelancerId(FREELANCER_SIREN, FREELANCER_NAME);
  AgencyId agencyId = new AgencyId(AGENCY_SIREN, AGENCY_NAME);
  Address agencyAddress = new Address("1", "Paris", "75010", "Rue du Poulet", "France");
  Address freelancerAddress = new Address("2", "Lyon", "75011", "Rue du Lamp", "France");

  StartWagePortageAgreement startWagePortageAgreement;

  @Autowired
  private WagePortageAgreementProvider wagePortageAgreementProvider;
  @Autowired
  private FreelancerProvider freelancerProvider;
  @Autowired
  private AgencyProvider agencyProvider;


  @BeforeAll
  void init() {
    startWagePortageAgreement = new StartWagePortageAgreement(wagePortageAgreementProvider, agencyProvider,
        freelancerProvider);
  }


  @Test
  void startWagePortageAgreement_mustProduceAProperWagePortageAgreement() throws StartWagePortageAgreementException {
    //ARRANGE :An agency and a freelancer are required for any wage portage agreement signature

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

    //ACT
    WagePortageAgreement registeredWagePortageAgreement = startWagePortageAgreement
        .execute(agencyId, freelancerId, WAGE_PORTAGE_TERMS, SERVICE_FEES_RATE);

    //ASSERT
    assertThat(registeredWagePortageAgreement).isNotNull();
    assertThat(registeredWagePortageAgreement.getWagePortageAgreementId()).isNotNull();
    assertThat(registeredWagePortageAgreement.getWagePortageAgreementId().getAgreementNumber()).isNotNull();

    assertThat(registeredWagePortageAgreement.getServiceFeesRate()).isGreaterThan(0);
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
