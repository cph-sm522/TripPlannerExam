package dat.controllers;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.List;

public class Populator {

    private final EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Trip> populateData() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Guide guide1 = new Guide(null, "John", "Doe", "john.doe@example.com", "123456789", 5, null);
            Guide guide2 = new Guide(null, "Jane", "Smith", "jane.smith@example.com", "987654321", 7, null);
            Guide guide3 = new Guide(null, "Bob", "Johnson", "bob.johnson@example.com", "456123789", 3, null);
            Guide guide4 = new Guide(null, "Alice", "Brown", "alice.brown@example.com", "789456123", 8, null);
            Guide guide5 = new Guide(null, "Mike", "Davis", "mike.davis@example.com", "321654987", 2, null);

            em.persist(guide1);
            em.persist(guide2);
            em.persist(guide3);
            em.persist(guide4);
            em.persist(guide5);

            em.getTransaction().commit();

            em.getTransaction().begin();

            Trip trip1 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Beach Point", "Beach Fun", 49.99, Category.BEACH, null);
            Trip trip2 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "City Center", "City Tour", 59.99, Category.CITY, null);
            Trip trip3 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(4), "Forest Trail", "Nature Walk", 39.99, Category.FOREST, null);
            Trip trip4 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(5), "Lake View", "Lake Sightseeing", 29.99, Category.LAKE, null);
            Trip trip5 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Sea Breeze", "Ocean Tour", 49.99, Category.SEA, null);
            Trip trip6 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(6), "Mountain Peak", "Snow Adventure", 69.99, Category.SNOW, null);
            Trip trip7 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Island Shore", "Beach Excursion", 45.99, Category.CITY, null);
            Trip trip8 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Downtown Stroll", "City Evening Walk", 54.99, Category.SEA, null);
            Trip trip9 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Cave Entrance", "Underground Discovery", 74.99, Category.FOREST, null);
            Trip trip10 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(5), "Harbor View", "Harbor Sailing", 65.00, Category.SEA, null);

            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);
            em.persist(trip4);
            em.persist(trip5);
            em.persist(trip6);
            em.persist(trip7);
            em.persist(trip8);
            em.persist(trip9);
            em.persist(trip10);

            em.getTransaction().commit();

            // Separate transaction for setting guides to trips
            em.getTransaction().begin();

            trip1.setGuide(guide1);
            trip2.setGuide(guide2);
            trip3.setGuide(guide3);
            trip4.setGuide(guide4);
            trip5.setGuide(guide5);
            trip6.setGuide(guide5);
            trip7.setGuide(guide1);


            em.merge(trip1);
            em.merge(trip2);
            em.merge(trip3);
            em.merge(trip4);
            em.merge(trip5);
            em.merge(trip6);
            em.merge(trip7);

            em.getTransaction().commit();

            System.out.println("Data successfully persisted.");

            return List.of(trip1, trip2, trip3, trip4, trip5, trip6, trip7, trip8, trip9, trip10);
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            System.err.println("Failed to populate data: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public <T> void cleanup(Class<T> t) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Query deleteQuery = em.createQuery("DELETE FROM " + t.getSimpleName());
            deleteQuery.executeUpdate();
            em.getTransaction().commit();
            System.out.println("Entities of type " + t.getSimpleName() + " cleaned up successfully.");

        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            System.err.println("Failed to clean up entities of type " + t.getSimpleName() + ": " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public static UserDTO[] populateUsers(EntityManagerFactory emf) {

        User user = new User("usertest", "user123");
        User admin = new User("admintest", "admin123");

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Role userRole = em.find(Role.class, "USER");
            Role adminRole = em.find(Role.class, "ADMIN");

            if (userRole == null) {
                userRole = new Role("USER");
                em.persist(userRole);
            }
            if (adminRole == null) {
                adminRole = new Role("ADMIN");
                em.persist(adminRole);
            }

            user.addRole(userRole);
            admin.addRole(adminRole);

            em.persist(user);
            em.persist(admin);

            em.getTransaction().commit();
        }

        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }

}
