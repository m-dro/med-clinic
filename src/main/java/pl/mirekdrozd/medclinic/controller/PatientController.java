package pl.mirekdrozd.medclinic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.mirekdrozd.medclinic.service.PatientService;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoIn;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoOut;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
class PatientController {

    private final PatientService patientService;

    @PostMapping
    @ResponseStatus(CREATED)
    public PatientDtoOut create(@RequestBody PatientDtoIn patient) {
        return patientService.create(patient);
    }

    @GetMapping("/{id}")
    public PatientDtoOut retrieve(@PathVariable Long id) {
        return patientService.retrieveById(id);
    }

    @PutMapping("/{id}")
    public PatientDtoOut update(@PathVariable Long id, @RequestBody PatientDtoIn patient) {
        return patientService.update(id, patient);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }

}
