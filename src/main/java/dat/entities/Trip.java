package dat.entities;

import dat.dtos.TripDTO;
import dat.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private String startposition;
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "guide_id", nullable = true)
    private Guide guide;

    public Trip(TripDTO tripDTO) {
        this.id = tripDTO.getId();
        this.starttime = tripDTO.getStarttime();
        this.endtime = tripDTO.getEndtime();
        this.startposition = tripDTO.getStartposition();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
    }

    public Trip(Long id, LocalDateTime starttime, LocalDateTime endtime, String startposition, String name, double price, Category category, Guide guide) {
        this.id = id;
        this.starttime = starttime;
        this.endtime = endtime;
        this.startposition = startposition;
        this.name = name;
        this.price = price;
        this.category = category;
        this.guide = guide;
    }

    public Trip() {}

}