package pl.mirekdrozd.medclinic.repository;

import org.springframework.stereotype.Repository;
import pl.mirekdrozd.medclinic.repository.model.Patient;

@Repository
public interface PatientRepository extends AbstractRepository<Patient> {

    Long countByPesel(String pesel);

    default boolean isPeselTaken(String pesel) {
        if (countByPesel(pesel) > 0) {
            throw new IllegalArgumentException("This pesel is already taken");
        }
        return false;
    }
}
