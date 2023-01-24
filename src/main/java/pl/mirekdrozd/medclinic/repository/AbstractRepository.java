package pl.mirekdrozd.medclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;

@NoRepositoryBean
public interface AbstractRepository<T> extends JpaRepository<T, Long> {

    default T getOrThrow(Long id) {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    default void deleteOrThrow(Long id) {
        if (id == null) {
            throw new NullPointerException("Id must not be null");
        }
        deleteById(id);
    }
}
