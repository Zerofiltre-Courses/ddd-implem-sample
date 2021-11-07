package com.zerofiltre.freeland.infra.providers.database.freelancer.model;

import com.zerofiltre.freeland.domain.Address;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class FreelancerJPA {

  @Id
  private String siren;
  private String name;

  private String description;
  private String phoneNumber;

  @OneToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

}
