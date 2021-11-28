package tech.zerofiltre.freeland.domain.agency.model;

import tech.zerofiltre.freeland.domain.Address;


public class Agency {

  private AgencyId agencyId;
  private String description;
  private String phoneNumber;
  private Address address;

  public Agency() {
  }

  public Agency(AgencyId agencyId, String description, String phoneNumber, Address address) {
    this.agencyId = agencyId;
    this.description = description;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public AgencyId getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(AgencyId agencyId) {
    this.agencyId = agencyId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Agency register() {
    return this;
  }

  public Agency update() {
    return this;
  }

  public void close() {

  }

}
