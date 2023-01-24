package pl.mirekdrozd.medclinic.controller;

import io.restassured.builder.RequestSpecBuilder;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.mirekdrozd.medclinic.repository.model.Gender;
import pl.mirekdrozd.medclinic.service.dto.PatientDtoIn;
import pl.mirekdrozd.medclinic.utils.PeselGenerator;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.*;

public class PatientControllerIntegrationTest extends AbstractIntegrationTest {
    private static final String PATH = "/patients";

    @PostConstruct
    private void setSpec() {
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setPort(port)
                .setBasePath(contextPath)
                .setContentType(JSON)
                .setAuth(basic("testReceptionist", "recePass"))
                .build();
    }

    @PostConstruct
    private void config() {
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    PatientDtoIn getValidPatientDtoIn() {
        PatientDtoIn patientDtoIn = new PatientDtoIn();
        patientDtoIn.setFirstName(faker.name().firstName());
        patientDtoIn.setLastName(faker.name().lastName());
        patientDtoIn.setGender(faker.random().nextBoolean() ? "MALE" : "FEMALE");
        patientDtoIn.setDateOfBirth(LocalDate.now().minusYears(RandomUtils.nextInt(1, 80)).toString());
        patientDtoIn.setPesel(PeselGenerator.generateValidPesel());
        return patientDtoIn;
    }

    @Test
    @DisplayName("Test POST patient")
    void testCreatePatient() {
        PatientDtoIn validPatientDtoIn = getValidPatientDtoIn();

        given(spec)
                .body(validPatientDtoIn)
                .when()
                .post(PATH)
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .body("id", notNullValue())
                .body("firstName", equalTo(validPatientDtoIn.getFirstName()))
                .body("lastName", equalTo(validPatientDtoIn.getLastName()))
                .body("gender", equalTo(validPatientDtoIn.getGender()))
                .body("dateOfBirth", equalTo(validPatientDtoIn.getDateOfBirth()))
                .body("pesel", equalTo(validPatientDtoIn.getPesel()));
    }

    @Test
    @DisplayName("Test GET patient by ID")
    void testRetrievePatient() {
        int existingPatientId = 1;

        given(spec)
                .pathParam("id", existingPatientId)
                .when()
                .get(PATH + "/{id}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("id", equalTo(existingPatientId))
                .body("firstName", notNullValue())
                .body("lastName", notNullValue())
                .body("gender", notNullValue())
                .body("dateOfBirth", notNullValue())
                .body("pesel", notNullValue());
    }

    @Test
    @DisplayName("Test UPDATE patient by ID")
    void testUpdatePatient() {
        int existingPatientId = 1;
        PatientDtoIn patientDtoIn = getValidPatientDtoIn();

        given(spec)
                .body(patientDtoIn)
                .pathParam("id", existingPatientId)
                .when()
                .put(PATH + "/{id}")
                .then()
                .assertThat()
                .statusCode(OK.value())
                .body("firstName", equalTo(patientDtoIn.getFirstName()))
                .body("lastName", equalTo(patientDtoIn.getLastName()))
                .body("gender", equalTo(patientDtoIn.getGender()))
                .body("dateOfBirth", equalTo(patientDtoIn.getDateOfBirth()))
                .body("pesel", equalTo(patientDtoIn.getPesel()));
    }

    @Test
    @DisplayName("Test DELETE patient by ID")
    void testAddAndDeletePatientById() {
        PatientDtoIn validPatientDtoIn = getValidPatientDtoIn();

        String insertSql = String.format("INSERT INTO patient(first_name, last_name, gender_id, date_of_birth, pesel) VALUES ('%s', '%s', %d, '%s', '%s') RETURNING id",
                validPatientDtoIn.getFirstName(), validPatientDtoIn.getLastName(), Gender.valueOf(validPatientDtoIn.getGender()).getId(),
                validPatientDtoIn.getDateOfBirth(), validPatientDtoIn.getPesel());
        long existingPatientId = jdbcTemplate.queryForObject(insertSql, Map.of(), Long.class);

        given(spec)
                .pathParam("id", existingPatientId)
                .when()
                .delete(PATH + "/{id}")
                .then()
                .assertThat()
                .statusCode(NO_CONTENT.value());

        String selectSql = "SELECT COUNT(*) FROM patient WHERE id = " + existingPatientId;
        Long count = jdbcTemplate.queryForObject(selectSql, Map.of(), Long.class);

        assertEquals(0L, count);
    }

}
