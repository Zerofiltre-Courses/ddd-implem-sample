package com.zerofiltre.freeland.infra.providers.database.client.model;

import com.zerofiltre.freeland.domain.Address;
import com.zerofiltre.freeland.infra.providers.database.AddressJPA;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
public class ClientJPA {

  @Id
  @GeneratedValue
  Long id;

  private String siren;
  private String name;

  private String description;
  private String phoneNumber;

  @OneToOne
  @JoinColumn(name = "address_db_id")
  private AddressJPA address;

}
