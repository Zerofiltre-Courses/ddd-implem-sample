package com.zerofiltre.freeland.infra.providers.database.serviceContract.model;

import com.zerofiltre.freeland.domain.agency.model.AgencyId;
import com.zerofiltre.freeland.domain.freelancer.model.FreelancerId;
import com.zerofiltre.freeland.infra.providers.database.agency.model.AgencyJPA;
import com.zerofiltre.freeland.infra.providers.database.freelancer.model.FreelancerJPA;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String agreementNumber;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_siren", referencedColumnName = "client_siren")
  private FreelancerJPA freelancer;

  private AgencyJPA agency;

  private float serviceFeesRate;
  private String terms;
  private Date startDate;
  private Date endDate;
}
