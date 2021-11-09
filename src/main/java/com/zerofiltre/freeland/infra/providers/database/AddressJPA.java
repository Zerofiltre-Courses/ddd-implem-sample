package com.zerofiltre.freeland.infra.providers.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AddressJPA {

  @Id
  @GeneratedValue
  private Long id;

  private String streetNumber;
  private String streetName;
  private String city;
  private String postalCode;
  private String country;
}
