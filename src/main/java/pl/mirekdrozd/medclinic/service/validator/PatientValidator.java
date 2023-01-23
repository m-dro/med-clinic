package pl.mirekdrozd.medclinic.service.validator;

import pl.mirekdrozd.medclinic.exception.ValidationException;
import pl.mirekdrozd.medclinic.repository.model.Gender;
import pl.mirekdrozd.medclinic.service.dto.RequestPatientDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PatientValidator {
    private static final int VALID_PESEL_LENGTH = 11;
    private static final int FIRST_NAME_MIN_LENGTH = 2;
    private static final int FIRST_NAME_MAX_LENGTH = 50;
    private static final int LAST_NAME_MIN_LENGTH = 1;
    private static final int LAST_NAME_MAX_LENGTH = 50;
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final List<Violation> violations = new ArrayList<>();
    private final RequestPatientDto requestPatientDto;

    public PatientValidator(RequestPatientDto requestPatientDto) {
        this.requestPatientDto = requestPatientDto;
    }

    private void result() {
        if (violations.size() > 0) {
            throw new ValidationException("Invalid patient data", violations);
        }
    }

    public void validate() {
        nullCheck();
        this
                .validFirstName()
                .validLastName()
                .validDateOfBirth()
                .validGender()
                .validPesel()
                .result();
    }

    private void nullCheck() {
        boolean nullFound = isNull(requestPatientDto.getFirstName())
                || isNull(requestPatientDto.getLastName())
                || isNull(requestPatientDto.getGender())
                || isNull(requestPatientDto.getPesel())
                || isNull(requestPatientDto.getDateOfBirth());
        if (nullFound) {
            throw new ValidationException("Missing fields", violations);
        }
    }

    private boolean isNull(Object field) {
        return field == null;
    }

    private PatientValidator validFirstName() {
        String fieldName = "First name";
        String value = requestPatientDto.getFirstName();
        boolean valid = hasCorrectLength(fieldName, value, FIRST_NAME_MIN_LENGTH, FIRST_NAME_MAX_LENGTH)
                && isNotBlank(fieldName, value)
                && hasLeadingOrTrailingWhitespace(fieldName, value)
                && hasLettersOnly(fieldName, value);
        return this;
    }

    private PatientValidator validLastName() {
        String fieldName = "Last name";
        String value = requestPatientDto.getLastName();
        boolean valid = hasCorrectLength(fieldName, value, LAST_NAME_MIN_LENGTH, LAST_NAME_MAX_LENGTH)
                && isNotBlank(fieldName, value)
                && hasLeadingOrTrailingWhitespace(fieldName, value)
                && hasLettersOnly(fieldName, value);
        return this;
    }

    private PatientValidator validDateOfBirth() {
        String fieldName = "Date of birth";
        String value = requestPatientDto.getDateOfBirth();
        LocalDate parsedDate = parseDate(fieldName, value);
        if (parsedDate != null) {
            isInFuture(fieldName, parsedDate);
        }
        return this;
    }

    private PatientValidator validGender() {
        String fieldName = "Gender";
        String value = requestPatientDto.getGender().toUpperCase();
        hasAcceptableValue(fieldName, value);
        return this;
    }

    private PatientValidator validPesel() {
        String fieldName = "Pesel";
        String value = requestPatientDto.getPesel();
        boolean valid = hasNumbersOnly(fieldName, value)
                && hasCorrectLength(fieldName, value, VALID_PESEL_LENGTH, VALID_PESEL_LENGTH)
                && hasCorrectChecksum(fieldName, value);
        return this;
    }

    private <T> boolean isValid(Predicate<T> condition, String fieldName, T value, String requirement) {
        boolean isValid = condition.test(value);
        if (!isValid) {
            violations.add(new Violation(fieldName, requirement));
        }
        return isValid;
    }

    private boolean hasAcceptableValue(String fieldName, String value) {
        String requirement = "Gender must be one of the following values: " + Arrays.toString(Gender.values());
        Predicate<String> condition = gender -> Arrays.stream(Gender.values())
                .anyMatch(genderAsEnum -> gender.equals(genderAsEnum.name()));
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean isInFuture(String fieldName, LocalDate value) {
        String requirement = "Must not be in the future";
        Predicate<LocalDate> condition = date -> date.isBefore(LocalDate.now());
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean hasLettersOnly(String fieldName, String value) {
        String requirement = "Must contain letters only";
        Predicate<String> condition = text -> text.chars().allMatch(Character::isLetter);
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean hasNumbersOnly(String fieldName, String value) {
        String requirement = "Must contain numbers only";
        Predicate<String> condition = text -> text.chars().allMatch(Character::isDigit);
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean hasLeadingOrTrailingWhitespace(String fieldName, String value) {
        String requirement = "Must not contain leading or trailing whitespace";
        Predicate<String> condition = text -> text.strip().length() == text.length();
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean isNotBlank(String fieldName, String value) {
        String requirement = "Must not be empty or contain whitespace only";
        Predicate<String> condition = (text -> !text.isBlank());
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean hasCorrectLength(String fieldName, String value, int min, int max) {
        String requirement = checkRequirementForLimits(min, max);
        Predicate<String> condition = (text -> text.length() >= min && text.length() <= max);
        return isValid(condition, fieldName, value, requirement);
    }

    private String checkRequirementForLimits(int min, int max) {
        String requirement;
        if (min == max) {
            requirement = String.format("Must contain %d characters", min);
        } else {
            requirement = String.format("Must contain between %d and %d characters", min, max);
        }
        return requirement;
    }

    private boolean hasCorrectChecksum(String fieldName, String value) {
        String requirement = "Must have valid checksum";
        Predicate<String> condition = (this::doChecksumCalculation);
        return isValid(condition, fieldName, value, requirement);
    }

    private boolean doChecksumCalculation(String pesel) {
        int peselLength = pesel.length();
        if (peselLength != 11) {
            return false;
        }
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int digit, sum = 0, control;
        int checksum = Integer.parseInt(pesel.substring(peselLength - 1));
        for (int i = 0; i < peselLength - 1; i++) {
            char c = pesel.charAt(i);
            digit = Integer.parseInt(String.valueOf(c));
            sum += digit * weights[i];
        }
        control = 10 - (sum % 10);
        if (control == 10) {
            control = 0;
        }
        return (control == checksum);
    }

    private LocalDate parseDate(String fieldName, String dateAsString) {
        LocalDate parsedDate = null;
        try {
            parsedDate = LocalDate.parse(dateAsString, dateFormat);
        } catch (DateTimeParseException e) {
            violations.add(new Violation(fieldName, "Must be in the following format: " + dateFormat));
        }
        return parsedDate;
    }
}
