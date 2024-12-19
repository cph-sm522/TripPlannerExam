package dat.dtos;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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
}