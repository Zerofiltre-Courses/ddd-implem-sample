package com.zerofiltre.freeland.domain;

public class Rate {

  private final float price;
  private final Currency currency;
  private final Frequency frequency;

  public Rate(float price, Currency currency, Frequency frequency) {
    this.price = price;
    this.currency = currency;
    this.frequency = frequency;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Frequency getFrequency() {
    return frequency;
  }

  public enum Currency {
    EUR,
    USD,
    XAF
  }

  public enum Frequency {
    DAILY,
    MONTHLY,
    YEARLY
  }


  public float getPrice() {
    return price;
  }

}
