package pl.mirekdrozd.medclinic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mirekdrozd.medclinic.service.PatientService;
import pl.mirekdrozd.medclinic.service.dto.RequestPatientDto;
import pl.mirekdrozd.medclinic.service.dto.ResponsePatientDto;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
class PatientController {

    private final PatientService patientService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponsePatientDto create(@RequestBody RequestPatientDto patient) {
        return patientService.create(patient);
    }

    @GetMapping("/{id}")
    public ResponsePatientDto retrieve(@PathVariable Long id) {
        return patientService.retrieveById(id);
    }

    @PutMapping("/{id}")
    public ResponsePatientDto update(@PathVariable Long id, @RequestBody RequestPatientDto patient) {
        return patientService.update(id, patient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }

}
