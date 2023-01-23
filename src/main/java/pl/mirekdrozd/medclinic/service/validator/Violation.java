package pl.mirekdrozd.medclinic.service.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Violation {
    private final String field;
    private final String message;
}
