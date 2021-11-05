package com.zerofiltre.freeland.domain.entities;

import com.zerofiltre.freeland.domain.vo.Address;
import com.zerofiltre.freeland.domain.vo.AgencyId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agency {

  private AgencyId agencyId;
  private String description;
  private String phoneNumber;
  private Address address;

  public Agency register() {
    return this;
  }

  public Agency update() {
    return this;
  }

  public void close() {

  }

}
