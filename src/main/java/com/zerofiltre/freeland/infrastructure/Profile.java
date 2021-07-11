package com.zerofiltre.freeland.infrastructure;

import javax.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Profile {

  private long id;
  private String name;
}
