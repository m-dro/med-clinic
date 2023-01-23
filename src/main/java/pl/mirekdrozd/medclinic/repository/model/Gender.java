package pl.mirekdrozd.medclinic.repository.model;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum Gender {
    NON_BINARY(0),
    MALE(1),
    FEMALE(2);

    private static final Map<Integer, Gender> ID_TO_GENDER;

    static {
        ID_TO_GENDER = Stream.of(Gender.values())
                .collect(toMap(Gender::getId, identity()));
    }

    private Integer id;

    Gender(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Gender fromId(Integer id) {
        Gender gender = ID_TO_GENDER.get(id);
        if (gender == null) {
            throw new IllegalArgumentException("Gender ID [" + id
                    + "] not recognised.");
        }
        return gender;
    }
}
