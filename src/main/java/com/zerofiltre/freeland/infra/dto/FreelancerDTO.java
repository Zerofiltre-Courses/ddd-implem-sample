package com.zerofiltre.freeland.infra.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FreelancerDTO {

  @Id
  private String freelancerId;
  private String description;
  private String phoneNumber;
  private String address;

  private String agencyId;
  private String agreementId;

}
