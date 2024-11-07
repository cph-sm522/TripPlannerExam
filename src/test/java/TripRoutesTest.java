import dat.config.AppConfig;
import dat.config.HibernateConfig;
import dat.controllers.Populator;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.entities.Role;
import dat.security.entities.User;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TripRoutesTest {

    private static Javalin app;
    private static EntityManagerFactory emf;
    private static Populator populator;
    private static SecurityController securityController;
    private static SecurityDAO securityDAO;

    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;

    List<Trip> trips;
    private Trip trip1, trip2, trip3;

    private final String BASE_URL = "http://localhost:7007/api/trips";

    @BeforeAll
    void setUp() {
        emf = HibernateConfig.getEntityManagerFactory("trip_planner");
        app = AppConfig.startServer(7007);

        populator = new Populator(emf);

        securityDAO = new SecurityDAO(emf);
        securityController = SecurityController.getInstance();
    }

    @BeforeEach
    void insertToDB() {
        trips = populator.populateData();
        trip1 = trips.get(0);
        trip2 = trips.get(1);
        trip3 = trips.get(2);

        UserDTO[] users = Populator.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        populator.cleanup(Trip.class);
        populator.cleanup(Guide.class);
        populator.cleanup(User.class);
        populator.cleanup(Role.class);
    }

    @AfterAll
    void closeDown() {
        AppConfig.stopServer(app);
        emf.close();
    }

    @Test
    void testGetAllTrips() {
        TripDTO[] trips = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/")
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO[].class);

        assertThat(trips, arrayWithSize(10));
    }


    @Test
    void testGetTripById() {
        TripDTO trip = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/" + trip1.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(trip.getName(), equalTo(trip1.getName()));
        assertThat(trip.getId(), equalTo(trip1.getId()));
    }

    @Test
    void testCreateTrip() {
        TripDTO newTrip = new TripDTO(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "New Location", "New Trip", 99.99, Category.CITY, null);

        TripDTO createdTrip = given()
                .contentType("application/json")
                .header("Authorization", adminToken)
                .body(newTrip)
                .when()
                .post(BASE_URL + "/")
                .then()
                .statusCode(201)
                .extract()
                .as(TripDTO.class);

        assertThat(createdTrip.getName(), equalTo(newTrip.getName()));
        assertThat(createdTrip.getCategory(), equalTo(newTrip.getCategory()));
    }

    @Test
    void testUpdateTrip() {
        TripDTO tripToUpdate = new TripDTO(trip2.getId(), trip2.getStarttime(), trip2.getEndtime(), trip2.getStartposition(), "Updated Trip Name", trip2.getPrice(), trip2.getCategory(), trip2.getGuide() != null ? trip2.getGuide().getId() : null);

        given()
                .contentType("application/json")
                .header("Authorization", adminToken)
                .body(tripToUpdate)
                .when()
                .put(BASE_URL + "/" + tripToUpdate.getId())
                .then()
                .statusCode(204);

        TripDTO updatedTrip = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/" + tripToUpdate.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(updatedTrip.getName(), equalTo("Updated Trip Name"));
    }

    @Test
    void testDeleteTrip() {
        given()
                .when()
                .header("Authorization", adminToken)
                .delete(BASE_URL + "/" + trip3.getId())
                .then()
                .statusCode(204);

        given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/" + trip3.getId())
                .then()
                .statusCode(404);
    }

    @Test
    void testAddGuideToTrip() {
        Long guideId = trip1.getGuide() != null ? trip1.getGuide().getId() : 525L;

        given()
                .when()
                .header("Authorization", adminToken)
                .put(BASE_URL + "/" + trip1.getId() + "/guides/" + guideId)
                .then()
                .statusCode(204);

        TripDTO updatedTrip = given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/" + trip1.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(TripDTO.class);

        assertThat(updatedTrip.getGuideId(), equalTo(guideId));
    }

    @Test
    void testGetTotalPriceByGuide() {
        given()
                .when()
                .header("Authorization", userToken)
                .get(BASE_URL + "/guides/totalprice")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }
}