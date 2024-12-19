package dat.dtos;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;

import java.time.LocalDateTime;

public class TripDTO {

    private Long id;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private String startposition;
    private String name;
    private double price;
    private Category category;
    private Long guideId;

    public TripDTO(Long id, LocalDateTime starttime, LocalDateTime endtime, String startposition, String name, double price, Category category, Long guideId) {
        this.id = id;
        this.starttime = starttime;
        this.endtime = endtime;
        this.startposition = startposition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guideId = guideId;
    }

    public TripDTO() {}

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.starttime = trip.getStarttime();
        this.endtime = trip.getEndtime();
        this.startposition = trip.getStartposition();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guideId = (trip.getGuide() != null) ? trip.getGuide().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }

    public LocalDateTime getEndtime() {
        return endtime;
    }

    public String getStartposition() {
        return startposition;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public Long getGuideId() {
        return guideId;
    }
}