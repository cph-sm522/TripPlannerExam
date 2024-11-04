package dat.controllers;

import dat.dtos.TripDTO;
import dat.daos.TripDAO;
import dat.enums.Category;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripController {

    private final TripDAO tripDAO;

    public TripController(EntityManagerFactory emf) {
        this.tripDAO = new TripDAO(emf);
    }

    public void getAll(Context ctx) throws ApiException {
        List<TripDTO> trips = tripDAO.getAll();
        ctx.json(trips);
    }

    public void getById(Context ctx) throws ApiException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        TripDTO trip = tripDAO.getById(id);
        ctx.json(trip);
    }

    public void create(Context ctx) throws ApiException {
        TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
        TripDTO createdTrip = tripDAO.create(tripDTO);
        ctx.status(201).json(createdTrip);
    }

    public void update(Context ctx) throws ApiException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
        tripDAO.update(id, tripDTO);
        ctx.status(204);
    }

    public void delete(Context ctx) throws ApiException {
        Long id = Long.parseLong(ctx.pathParam("id"));
        TripDTO tripDTO = tripDAO.getById(id);
        tripDAO.delete(tripDTO);
        ctx.status(204);
    }

    public void addGuideToTrip(Context ctx) throws ApiException {
        Long tripId = Long.parseLong(ctx.pathParam("tripId"));
        Long guideId = Long.parseLong(ctx.pathParam("guideId"));
        tripDAO.addGuideToTrip(tripId, guideId);
        ctx.status(204);
    }

    public void populate(Context ctx) {
        Populator.populateData();
        ctx.status(201).json("{\"message\": \"Database populated with sample trips and guides\"}");
    }

    public void getTripsByCategory(Context ctx) throws ApiException {
        String categoryParam = ctx.queryParam("category");
        System.out.println("Received categoryParam: " + categoryParam); // Debugging line
        if (categoryParam == null) {
            throw new ApiException(400, "Category query parameter is required");
        }
        Category category;
        try {
            category = Category.valueOf(categoryParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid category");
        }

        List<TripDTO> trips = tripDAO.getAll().stream()
                .filter(trip -> trip.getCategory() == category)
                .collect(Collectors.toList());
        ctx.json(trips);
    }

    public void getTotalPriceByGuide(Context ctx) throws ApiException {
        List<Map<String, Object>> totalPriceList = tripDAO.getTotalPriceByGuide();
        ctx.json(totalPriceList);
    }
}