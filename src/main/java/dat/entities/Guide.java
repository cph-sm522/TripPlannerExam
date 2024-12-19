package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "guide")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guide")
    private List<Trip> trips;

    public Guide() {}

    public Guide(Long id, String firstname, String lastname, String email, String phone, int yearsOfExperience, List<Trip> trips) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
        this.trips = trips;
    }
}