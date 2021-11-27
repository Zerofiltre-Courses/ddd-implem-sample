package tech.zerofiltre.freeland.domain;

public class Rate {

  private final float value;
  private final Currency currency;
  private final Frequency frequency;

  public Rate(float value, Currency currency, Frequency frequency) {
    this.value = value;
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


  public float getValue() {
    return value;
  }

}
