package dat.controllers;

import dat.dtos.TripDTO;
import dat.daos.TripDAO;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class TripController {

    private final TripDAO tripDAO;
    private final Populator populator;

    public TripController(EntityManagerFactory emf) {
        this.tripDAO = new TripDAO(emf);
        this.populator = new Populator(emf);
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
        ctx.status(204).json(tripDTO);
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
        TripDTO tripDTO = tripDAO.getById(tripId);
        ctx.status(204).json(tripDTO);
    }

    public void populate(Context ctx) {
        populator.populateData();
        ctx.status(201).json("{\"message\": \"Database populated with trips and guides\"}");
    }

    public void getTotalPriceByGuide(Context ctx) throws ApiException {
        List<Map<String, Object>> totalPriceList = tripDAO.getTotalPriceByGuide();
        ctx.json(totalPriceList);
    }
}