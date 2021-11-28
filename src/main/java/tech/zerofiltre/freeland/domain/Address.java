package tech.zerofiltre.freeland.domain;

public class Address {

  private final String streetNumber;
  private final String streetName;
  private final String city;
  private final String postalCode;
  private final String country;

  public Address(String streetNumber, String streetName, String postalCode, String city, String country) {
    this.streetNumber = streetNumber;
    this.streetName = streetName;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }

  public String getStreetNumber() {
    return streetNumber;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getStreetName() {
    return streetName;
  }
}
