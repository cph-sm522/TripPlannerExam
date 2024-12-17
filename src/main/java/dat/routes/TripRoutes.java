package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {

    private final TripController tripController;

    public TripRoutes() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trip_planner");
        this.tripController = new TripController(emf);
    }

    public EndpointGroup getTripRoutes() {
        return () -> {
            get("/trips", tripController::getAll, Role.USER);
            get("/trips/{id}", tripController::getById, Role.USER);
            post("/trips", tripController::create, Role.ADMIN);
            put("/trips/{id}", tripController::update, Role.ADMIN);
            delete("/trips/{id}", tripController::delete, Role.ADMIN);
            put("/trips/{tripId}/guides/{guideId}", tripController::addGuideToTrip, Role.ADMIN);
            post("/trips/populate", tripController::populate, Role.ADMIN);
            get("/trips/guides/totalprice", tripController::getTotalPriceByGuide, Role.USER);
        };
    }
}