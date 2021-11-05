package com.zerofiltre.freeland.domain.entities;

import com.zerofiltre.freeland.domain.vo.Address;
import com.zerofiltre.freeland.domain.vo.ClientId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {

  private ClientId clientId;
  private String description;
  private String phoneNumber;
  private Address address;

  public Client register() {
    return this;
  }

  public Client update() {
    return this;
  }

  public void delete() {

  }

}
