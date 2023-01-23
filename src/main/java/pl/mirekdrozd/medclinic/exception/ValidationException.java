package pl.mirekdrozd.medclinic.exception;

import lombok.Getter;
import pl.mirekdrozd.medclinic.service.validator.Violation;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<Violation> violations;

    public ValidationException(String message, List<Violation> violations) {
        super(message);
        this.violations = violations;
    }
}
