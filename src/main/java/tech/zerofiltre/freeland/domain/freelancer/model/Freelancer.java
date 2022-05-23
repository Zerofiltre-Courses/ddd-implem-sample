package tech.zerofiltre.freeland.domain.freelancer.model;

import tech.zerofiltre.freeland.domain.Address;
import tech.zerofiltre.freeland.domain.serviceContract.model.WagePortageAgreementId;
import java.util.ArrayList;
import java.util.List;


public class Freelancer {

  private FreelancerId freelancerId;
  private String description;
  private String phoneNumber;
  private Address address;

  private WagePortageAgreementId wagePortageAgreementId;
  private List<Skill> skills;


  public Freelancer(FreelancerId freelancerId, String description, String phoneNumber,
      Address address, WagePortageAgreementId wagePortageAgreementId,
      List<Skill> skills) {
    this.freelancerId = freelancerId;
    this.description = description;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.wagePortageAgreementId = wagePortageAgreementId;
    this.skills = skills;
  }

  public FreelancerId getFreelancerId() {
    return freelancerId;
  }

  private void setFreelancerId(FreelancerId freelancerId) {
    this.freelancerId = freelancerId;
  }

  public String getDescription() {
    return description;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  private void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Address getAddress() {
    return address;
  }

  private void setAddress(Address address) {
    this.address = address;
  }

  public WagePortageAgreementId getAgreementId() {
    return wagePortageAgreementId;
  }

  private void setAgreementId(WagePortageAgreementId wagePortageAgreementId) {
    this.wagePortageAgreementId = wagePortageAgreementId;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  private void setSkills(List<Skill> skills) {
    this.skills = skills;
  }

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
