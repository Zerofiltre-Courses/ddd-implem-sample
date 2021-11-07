package com.zerofiltre.freeland.domain.client.model;

import com.zerofiltre.freeland.domain.Address;


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

  public Client() {
  }

  public ClientId getClientId() {
    return clientId;
  }

  public void setClientId(ClientId clientId) {
    this.clientId = clientId;
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

  public Client register() {
    return this;
  }

  public Client update() {
    return this;
  }

  public void delete() {

  }

}
