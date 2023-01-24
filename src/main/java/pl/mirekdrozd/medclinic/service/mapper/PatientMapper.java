package pl.mirekdrozd.medclinic.service.mapper;

import org.mapstruct.*;
import pl.mirekdrozd.medclinic.repository.model.Patient;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoIn;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoOut;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDtoOut toDto(Patient patient);

    Patient toEntity(PatientDtoIn patientDto);

    void updateEntityFromDto(PatientDtoIn patient, @MappingTarget Patient retrievedPatient);

}
