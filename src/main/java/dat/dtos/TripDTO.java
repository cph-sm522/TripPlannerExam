package dat.dtos;

import dat.entities.Trip;
import dat.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {

    private Long id;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private String startposition;
    private String name;
    private double price;
    private Category category;
    private Long guideId;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.starttime = trip.getStarttime();
        this.endtime = trip.getEndtime();
        this.startposition = trip.getStartposition();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guideId = (trip.getGuide() != null) ? trip.getGuide().getId() : null;    }
}