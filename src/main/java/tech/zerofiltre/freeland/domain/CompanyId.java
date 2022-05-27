package tech.zerofiltre.freeland.domain;


public class CompanyId {

  private final String siren;
  private final String name;

  public CompanyId(String siren, String name) {
    this.siren = siren;
    this.name = name;
  }

  public String getSiren() {
    return siren;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "CompanyId{" +
            "siren='" + siren + '\'' +
            ", name='" + name + '\'' +
            '}';
  }
}
