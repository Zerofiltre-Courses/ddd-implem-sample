package com.zerofiltre.freeland.infra.providers.database;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class CompanyJPA {

  @Id
  private String siren;
  private String name;

  private String description;
  private String phoneNumber;

  private String streetNumber;
  private String streetName;
  private String city;
  private String postalCode;
  private String country;

}
