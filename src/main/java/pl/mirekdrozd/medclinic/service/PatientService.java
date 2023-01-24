package pl.mirekdrozd.medclinic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mirekdrozd.medclinic.repository.PatientRepository;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoIn;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoOut;
import pl.mirekdrozd.medclinic.service.mapper.PatientMapper;
import pl.mirekdrozd.medclinic.service.validator.PatientValidator;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientDtoOut create(PatientDtoIn patientDtoIn) {
        patientRepository.isPeselTaken(patientDtoIn.getPesel());
        new PatientValidator(patientDtoIn).validate();

        var mappedPatient = patientMapper.toEntity(patientDtoIn);
        var savedPatient = patientRepository.save(mappedPatient);

        return patientMapper.toDto(savedPatient);
    }

    public PatientDtoOut retrieveById(Long id) {
        var patient = patientRepository.getOrThrow(id);
        return patientMapper.toDto(patient);
    }


    public PatientDtoOut update(Long id, PatientDtoIn updatedPatient) {
        var retrievedPatient = patientRepository.getOrThrow(id);
        if (!updatedPatient.getPesel().equals(retrievedPatient.getPesel())) {
            patientRepository.isPeselTaken(updatedPatient.getPesel());
        }
        new PatientValidator(updatedPatient).validate();

        patientMapper.updateEntityFromDto(updatedPatient, retrievedPatient);
        return patientMapper.toDto(retrievedPatient);
    }


    public void delete(Long id) {
        patientRepository.deleteOrThrow(id);
    }

}
