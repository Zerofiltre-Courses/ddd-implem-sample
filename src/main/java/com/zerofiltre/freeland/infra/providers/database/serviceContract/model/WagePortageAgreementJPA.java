package com.zerofiltre.freeland.infra.providers.database.serviceContract.model;

import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
  Long id;

  private String agreementNumber;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "freelancer_db_id")
  private FreelancerJPA freelancer;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "agency_db_id")
  private AgencyJPA agency;

  private float serviceFeesRate;
  private String terms;
  private Date startDate;
  private Date endDate;
}
