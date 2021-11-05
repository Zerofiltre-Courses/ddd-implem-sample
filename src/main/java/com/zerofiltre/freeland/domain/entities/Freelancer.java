package com.zerofiltre.freeland.domain.entities;

import com.zerofiltre.freeland.domain.vo.Address;
import com.zerofiltre.freeland.domain.vo.AgencyId;
import com.zerofiltre.freeland.domain.vo.AgreementId;
import com.zerofiltre.freeland.domain.vo.FreelancerId;
import com.zerofiltre.freeland.domain.vo.Skill;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Freelancer {

  private FreelancerId freelancerId;
  private String description;
  private String phoneNumber;
  private Address address;

  private AgencyId agencyId;
  private AgreementId agreementId;
  private List<Skill> skills = new ArrayList<>();

  private Freelancer register() {
    return this;
  }

  private void delete() {

  }

  private void addSkill(Skill skill) {
    this.skills.add(skill);
  }

  private void removeSkill(Skill skill) {
    this.skills.remove(skill);

  }

}
