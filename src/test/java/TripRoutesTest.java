import dat.config.AppConfig;
import dat.config.HibernateConfig;
import dat.controllers.TripController;
import dat.daos.TripDAO;
import dat.dtos.TripDTO;
import dat.entities.Trip;
import dat.enums.Category;
import dat.exceptions.ApiException;
import dat.controllers.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripRoutesTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static TripDAO tripDAO;
    private static Populator populator;

    private final String BASE_URL = "http://localhost:7000/api/trips";

    private List<Trip> trips;
    private Trip t1, t2, t3;

    @BeforeAll
    void init() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = AppConfig.startServer(7007);
        tripDAO = new TripDAO(emf);
        populator = new Populator();
        RestAssured.baseURI = "http://localhost:7000";
    }

    @BeforeEach
    void setUp() throws ApiException {
        List<Trip> trips = populator.populateData();
        t1 = trips.get(0);
        t2 = trips.get(1);
        t3 = trips.get(2);

        tripDAO.create(new TripDTO(t1));
        tripDAO.create(new TripDTO(t2));
        tripDAO.create(new TripDTO(t3));
    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Trip.class);
    }

    @AfterAll
    void closeDown() {
        AppConfig.stopServer(app);
    }

    @Test
    void testGetAllTrips() {
        TripDTO[] trips = given()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO[].class);

        assertThat(trips, arrayWithSize(3));
        assertThat(trips, arrayContainingInAnyOrder(new TripDTO(t1), new TripDTO(t2), new TripDTO(t3)));
    }

    @Test
    void testGetTripById() {
        TripDTO trip = given()
                .when()
                .get(BASE_URL + "/" + t1.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(trip.getName(), equalTo(t1.getName()));
        assertThat(trip.getId(), equalTo(t1.getId()));
    }

    @Test
    void testCreateTrip() throws ApiException {
        TripDTO newTrip = new TripDTO(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "New Location", "New Trip", 99.99, Category.CITY, null);

        TripDTO createdTrip = given()
                .contentType("application/json")
                .body(newTrip)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .extract()
                .as(TripDTO.class);

        assertThat(createdTrip.getName(), equalTo(newTrip.getName()));
        assertThat(tripDAO.getAll(), hasSize(4));
    }

    @Test
    void testUpdateTrip() {
        TripDTO updatedTrip = new TripDTO(t2);
        updatedTrip.setName("Updated Trip");

        TripDTO trip = given()
                .contentType("application/json")
                .body(updatedTrip)
                .when()
                .put(BASE_URL + "/" + t2.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(trip.getName(), equalTo("Updated Trip"));
    }

    @Test
    void testDeleteTrip() throws ApiException {
        given()
                .when()
                .delete(BASE_URL + "/" + t3.getId())
                .then()
                .statusCode(204);

        assertThat(tripDAO.getAll(), hasSize(2));
        assertThat(tripDAO.getAll(), not(hasItem(new TripDTO(t3))));
    }

    @Test
    void testAddGuideToTrip() throws ApiException {
        Long guideId = 1L;
        given()
                .when()
                .put(BASE_URL + "/" + t1.getId() + "/guides/" + guideId)
                .then()
                .statusCode(204);

        TripDTO trip = tripDAO.getById(t1.getId());
        assertThat(trip.getGuideId(), equalTo(guideId));
    }

    @Test
    void testPopulateTrips() {
        given()
                .when()
                .post(BASE_URL + "/populate")
                .then()
                .statusCode(201)
                .body("message", equalTo("Database populated with sample trips and guides"));
    }

    @Test
    void testGetTotalPriceByGuide() {
        given()
                .when()
                .get(BASE_URL + "/guides/totalprice")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}
