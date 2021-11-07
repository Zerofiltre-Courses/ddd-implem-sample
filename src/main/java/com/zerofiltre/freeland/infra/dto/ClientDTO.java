package com.zerofiltre.freeland.infra.dto;

import com.zerofiltre.freeland.domain.vo.Address;
import com.zerofiltre.freeland.domain.vo.ClientId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientDTO {
  @Id
  private String clientId;
  private String description;
  private String phoneNumber;
  private String address;
}
