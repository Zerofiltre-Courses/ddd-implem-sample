package com.zerofiltre.freeland.infra.dto;

import com.zerofiltre.freeland.domain.vo.Rate;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceContractDTO {

  @Id
  private String contractId;
  private String freelancerId;
  private String clientId;
  private String agencyId;
  private String rate;
  private String terms;
  private Date startDate;
  private Date endDate;

}
