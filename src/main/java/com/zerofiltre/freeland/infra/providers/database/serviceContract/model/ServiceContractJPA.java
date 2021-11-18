package com.zerofiltre.freeland.infra.providers.database.serviceContract.model;

import com.zerofiltre.freeland.domain.Rate.Currency;
import com.zerofiltre.freeland.domain.Rate.Frequency;
import com.zerofiltre.freeland.infra.providers.database.client.model.ClientJPA;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_contract")
public class ServiceContractJPA {

  @Id
  @GeneratedValue
  private Long contractNumber;

  @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JoinColumn(name = "wage_portage_agreement_number")
  private WagePortageAgreementJPA wagePortageAgreement;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_siren")
  private ClientJPA client;

  private float ratePrice;
  private Currency rateCurrency;
  private Frequency rateFrequency;

  private String terms;
  private Date startDate;
  private Date endDate;

}
