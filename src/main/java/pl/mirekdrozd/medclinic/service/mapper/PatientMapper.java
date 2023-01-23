package pl.mirekdrozd.medclinic.service.mapper;

import org.mapstruct.*;
import pl.mirekdrozd.medclinic.repository.model.Patient;
import pl.mirekdrozd.medclinic.service.dto.RequestPatientDto;
import pl.mirekdrozd.medclinic.service.dto.ResponsePatientDto;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(source = "firstName", target = "requestPatientDto.firstName")
    @Mapping(source = "lastName", target = "requestPatientDto.lastName")
    @Mapping(source = "gender", target = "requestPatientDto.gender")
    @Mapping(source = "pesel", target = "requestPatientDto.pesel")
    @Mapping(source = "dateOfBirth", target = "requestPatientDto.dateOfBirth")
    ResponsePatientDto toDto(Patient patient);

    Patient toEntity(RequestPatientDto patientDto);

    void updateEntityFromDto(RequestPatientDto patient, @MappingTarget Patient retrievedPatient);

}
