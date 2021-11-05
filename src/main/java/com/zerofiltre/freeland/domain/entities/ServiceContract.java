package com.zerofiltre.freeland.domain.entities;

import com.zerofiltre.freeland.domain.vo.AgencyId;
import com.zerofiltre.freeland.domain.vo.ClientId;
import com.zerofiltre.freeland.domain.vo.ContractId;
import com.zerofiltre.freeland.domain.vo.FreelancerId;
import com.zerofiltre.freeland.domain.vo.Rate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceContract {

  private ContractId contractId;

  private FreelancerId freelancerId;
  private ClientId clientId;
  private AgencyId agencyId;
  private Rate rate;
  private String terms;
  private Date startDate;
  private Date endDate;


  public ServiceContract update() {
    return this;
  }

  public void start() {
    this.startDate = new Date();
  }


  public void end() {
    this.endDate = new Date();
  }


}
