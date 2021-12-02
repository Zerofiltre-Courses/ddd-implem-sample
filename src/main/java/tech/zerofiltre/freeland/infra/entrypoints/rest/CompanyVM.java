package tech.zerofiltre.freeland.infra.entrypoints.rest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CompanyVM {

    private String siren;
    private String name;

    private String description;
    private String phoneNumber;

    private String streetNumber;
    private String streetName;
    private String city;
    private String postalCode;
    private String country;

}
