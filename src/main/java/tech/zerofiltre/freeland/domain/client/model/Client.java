package tech.zerofiltre.freeland.domain.client.model;

import tech.zerofiltre.freeland.domain.Address;


public class Client {

  private ClientId clientId;
  private String description;
  private String phoneNumber;
  private Address address;

  public Client(ClientId clientId, String description, String phoneNumber,
      Address address) {
    this.clientId = clientId;
    this.description = description;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  public ClientId getClientId() {
    return clientId;
  }

  private void setClientId(ClientId clientId) {
    this.clientId = clientId;
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

  public Client register() {
    return this;
  }

  public Client update() {
    return this;
  }

  public void delete() {

  }

}
