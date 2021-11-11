package com.zerofiltre.freeland.infra.providers.database.serviceContract.model;

import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WagePortageAgreementJPA {

  @Id
  @GeneratedValue
  private Long agreementNumber;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "freelancer_siren")
  private FreelancerJPA freelancer;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "agency_siren")
  private AgencyJPA agency;

  private float serviceFeesRate;
  private String terms;
  private Date startDate;
  private Date endDate;
}
