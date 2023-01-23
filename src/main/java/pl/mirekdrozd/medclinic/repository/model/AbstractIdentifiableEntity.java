package pl.mirekdrozd.medclinic.repository.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractIdentifiableEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    protected Long id;
}
