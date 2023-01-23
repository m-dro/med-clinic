package pl.mirekdrozd.medclinic.service.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePatientDto {

    private Long id;

    @JsonUnwrapped
    RequestPatientDto requestPatientDto;

}
