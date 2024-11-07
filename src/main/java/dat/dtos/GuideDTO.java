package dat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

}