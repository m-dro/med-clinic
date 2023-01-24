package pl.mirekdrozd.medclinic.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatientBasicDto {
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String pesel;
}
