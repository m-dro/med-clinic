package pl.mirekdrozd.medclinic.service.validator;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.mirekdrozd.medclinic.exception.ValidationException;
import pl.mirekdrozd.medclinic.repository.model.Gender;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoIn;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

class PatientValidatorTest {

    PatientValidator patientValidator;
    PatientDtoIn patientDtoIn;

    private PatientDtoIn getValidRequestPatientDto() {
        patientDtoIn = new PatientDtoIn();
        patientDtoIn.setFirstName("Jacek");
        patientDtoIn.setLastName("Placek");
        patientDtoIn.setGender("MALE");
        patientDtoIn.setPesel("92012674117");
        patientDtoIn.setDateOfBirth("1995-05-12");
        return patientDtoIn;
    }

    private PatientValidator getSimplePatientValidator(PatientDtoIn patientDtoIn) {
        return new PatientValidator(patientDtoIn);
    }

    @BeforeEach
    void setUp() {
        patientDtoIn = getValidRequestPatientDto();
        patientValidator = getSimplePatientValidator(patientDtoIn);
    }

    @Test
    @DisplayName("Should NOT throw any exception for valid patient")
    void shouldNotThrowAnyException() {
        assertThatNoException().isThrownBy(patientValidator::validate);
    }

    @Test
    @DisplayName("Should throw ValidationException with specific message if firstName in RequestPatientDto is null")
    void shouldThrowIfFirstNameIsNull() {
        //given
        patientDtoIn.setFirstName(null);
        //when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing fields");
    }

    @Test
    @DisplayName("Should throw ValidationException with specific message if lastName in RequestPatientDto is null")
    void shouldThrowIfLastNameIsNull() {
        //given
        patientDtoIn.setLastName(null);
        //when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing fields");
    }

    @Test
    @DisplayName("Should throw ValidationException with specific message if dateOfBirth in RequestPatientDto is null")
    void shouldThrowIfDateOfBirthIsNull() {
        //given
        patientDtoIn.setDateOfBirth(null);
        //when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing fields");
    }

    @Test
    @DisplayName("Should throw ValidationException with specific message if pesel in RequestPatientDto is null")
    void shouldThrowIfPeselIsNull() {
        //given
        patientDtoIn.setPesel(null);
        //when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing fields");
    }

    @Test
    @DisplayName("Should throw ValidationException with specific message if gender in RequestPatientDto is null")
    void shouldThrowIfGenderIsNull() {
        //given
        patientDtoIn.setGender(null);
        //when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Missing fields");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "1", "this_string_has_51_characters_and_limit_is_50______"})
    @DisplayName("Should throw exception if first name has incorrect length")
    void shouldThrowExceptionIfFirstNameHasIncorrectLength(String firstName) {
        // given
        patientDtoIn.setFirstName(firstName);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid patient data")
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "               "})
    @DisplayName("Should throw ValidationException if first name is blank")
    void shouldThrowValidationExceptionIfFirstNameIsBlank(String firstName) {
        // given
        patientDtoIn.setFirstName(firstName);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .hasMessage("Invalid patient data")
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("First name", "Must not be empty or contain whitespace only"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Jan12", "12Jan", "_Jan", "Jan_", "Ja_n"})
    @DisplayName("Should throw ValidationException if first name includes non-letter characters")
    void shouldThrowValidationExceptionIfFirstNameIncludesNonLetterCharacters(String firstName) {
        // given
        patientDtoIn.setFirstName(firstName);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("First name", "Must contain letters only"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"  leading", "trailing   ", "   leading_and_trailing   "})
    @DisplayName("Should throw ValidationException if first name has leading and/or trailing whitespace")
    void shouldThrowValidationExceptionIfFirstNameHasLeadingOrTrailingWhitespace(String firstName) {
        // given
        patientDtoIn.setFirstName(firstName);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("First name", "Must not contain leading or trailing whitespace"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"02070803627", "68040234790", "52032786251"})
    @DisplayName("Should throw ValidationException if pesel has incorrect checksum")
    void shouldThrowValidationExceptionIfPeselHasIncorrectChecksum(String pesel) {
        // given
        patientDtoIn.setPesel(pesel);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("Pesel", "Must have valid checksum"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/valid-pesels.csv", numLinesToSkip = 1)
    @DisplayName("Should NOT throw ValidationException if pesel has correct checksum")
    void shouldNotThrowValidationExceptionIfPeselHasCorrectChecksum(String pesel) {
        // given
        patientDtoIn.setPesel(pesel);
        // when & then
        assertThatNoException().isThrownBy(patientValidator::validate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MALEE", "FFEMALE", "NONBINARY"})
    @DisplayName("Should throw ValidationException if gender has invalid value")
    void shouldThrowValidationExceptionIfGenderHasInvalidValue(String gender) {
        // given
        patientDtoIn.setGender(gender);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("Gender", "Gender must be one of the following values: " + Arrays.toString(Gender.values())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"MALE", "FEMALE", "NON_BINARY"})
    @DisplayName("Should NOT throw ValidationException if gender has acceptable value")
    void shouldNotThrowValidationExceptionIfGenderHasAcceptableValue(String gender) {
        // given
        patientDtoIn.setGender(gender);
        // when & then
        assertThatNoException().isThrownBy(patientValidator::validate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2100-01-10", "2221-10-16"})
    @DisplayName("Should throw exception if date of birth is in the future")
    void shouldThrowExceptionIfDateOfBirthIsInTheFuture(String dateOfBirth) {
        // given
        patientDtoIn.setDateOfBirth(dateOfBirth);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("Date of birth", "Must not be in the future"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2019-12-01", "2021-10-10"})
    @DisplayName("Should NOT throw ValidationException if date of birth has acceptable value")
    void shouldNotThrowValidationExceptionIfDateOfBirthHasAcceptableValue(String dateOfBirth) {
        // given
        patientDtoIn.setDateOfBirth(dateOfBirth);
        // when & then
        assertThatNoException().isThrownBy(patientValidator::validate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"X8021173355", "5802117335X", "58021X73355", "5802117335_", "_8021173355", "58021_73355"})
    @DisplayName("Should throw ValidationException if pesel contains non-numeric characters")
    void shouldThrowValidationExceptionIfPeselContainsNonNumericCharacters(String pesel) {
        // given
        patientDtoIn.setPesel(pesel);
        // when & then
        assertThatThrownBy(patientValidator::validate)
                .isInstanceOf(ValidationException.class)
                .extracting("violations", as(InstanceOfAssertFactories.list(Violation.class)))
                .extracting(Violation::getField, Violation::getMessage)
                .containsOnly(tuple("Pesel", "Must contain numbers only"));
    }
}