package pl.mirekdrozd.medclinic.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPatientDto {

    private String firstName;

    private String lastName;

    private String gender;

    private String dateOfBirth;

    private String pesel;
}
