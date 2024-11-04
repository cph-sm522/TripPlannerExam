package dat.dtos;

import dat.entities.Guide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;
    private List<TripDTO> trips;

    public GuideDTO(Guide guide) {
        if (guide == null) {
            throw new IllegalArgumentException("Guide cannot be null");
        }
        this.id = guide.getId();
        this.firstname = guide.getFirstname();
        this.lastname = guide.getLastname();
        this.email = guide.getEmail();
        this.phone = guide.getPhone();
        this.yearsOfExperience = guide.getYearsOfExperience();
        this.trips = guide.getTrips().stream()
                .map(TripDTO::new)
                .collect(Collectors.toList());
    }
}