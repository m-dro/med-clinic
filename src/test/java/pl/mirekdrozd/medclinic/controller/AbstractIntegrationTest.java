package pl.mirekdrozd.medclinic.controller;

import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
    protected static final String BASE_URI = "http://localhost";

    @LocalServerPort
    protected int port;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    @Autowired
    protected NamedParameterJdbcOperations jdbcTemplate;

    protected final Faker faker = new Faker();

    protected RequestSpecification spec;
}
