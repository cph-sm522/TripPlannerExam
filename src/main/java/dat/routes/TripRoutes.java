package dat.routes;

import dat.config.HibernateConfig;
import dat.controllers.TripController;
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
            get("/trips", tripController::getAll);
            get("/trips/{id}", tripController::getById);
            post("/trips", tripController::create);
            put("/trips/{id}", tripController::update);
            delete("/trips/{id}", tripController::delete);
            put("/trips/{tripId}/guides/{guideId}", tripController::addGuideToTrip);
            post("/trips/populate", tripController::populate);
            get("/trips/category-filter", tripController::getTripsByCategory);
            get("/trips/guides/totalprice", tripController::getTotalPriceByGuide);
        };
    }
}
