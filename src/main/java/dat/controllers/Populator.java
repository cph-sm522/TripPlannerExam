package dat.controllers;

import dat.config.HibernateConfig;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Populator {

    public static void main(String[] args) {
        populateData();
    }

    public static List<Trip> populateData() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trip_planner");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();


        Guide guide1 = new Guide(null, "John", "Doe", "john.doe@example.com", "123456789", 5, null);
        Guide guide2 = new Guide(null, "Jane", "Smith", "jane.smith@example.com", "987654321", 7, null);
        Guide guide3 = new Guide(null, "Bob", "Johnson", "bob.johnson@example.com", "456123789", 3, null);
        Guide guide4 = new Guide(null, "Alice", "Brown", "alice.brown@example.com", "789456123", 8, null);
        Guide guide5 = new Guide(null, "Mike", "Davis", "mike.davis@example.com", "321654987", 2, null);

        Trip trip1 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Beach Point", "Beach Fun", 49.99, Category.BEACH, guide1);
        Trip trip2 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "City Center", "City Tour", 59.99, Category.CITY, guide2);
        Trip trip3 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(4), "Forest Trail", "Nature Walk", 39.99, Category.FOREST, guide3);
        Trip trip4 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(5), "Lake View", "Lake Sightseeing", 29.99, Category.LAKE, guide4);
        Trip trip5 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "Sea Breeze", "Ocean Tour", 49.99, Category.SEA, guide5);
        Trip trip6 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(6), "Mountain Peak", "Snow Adventure", 69.99, Category.SNOW, guide5);
        Trip trip7 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Island Shore", "Beach Excursion", 45.99, Category.CITY, guide1);
        Trip trip8 = new Trip(null, LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Downtown Stroll", "City Evening Walk", 54.99, Category.SEA, guide2);

        guide1.setTrips(Arrays.asList(trip1, trip7));
        guide2.setTrips(Arrays.asList(trip2, trip8));
        guide3.setTrips(List.of(trip3));
        guide4.setTrips(List.of(trip4));
        guide5.setTrips(Arrays.asList(trip5, trip6));

        em.persist(guide1);
        em.persist(guide2);
        em.persist(guide3);
        em.persist(guide4);
        em.persist(guide5);

        em.getTransaction().commit();
        System.out.println("Data successfully persisted.");

        return List.of(trip1, trip2, trip3, trip4, trip5, trip6, trip7, trip8);
    }

    public <T> void cleanup(Class<T> entityType) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trip_planner");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query deleteQuery = em.createQuery("DELETE FROM " + entityType.getSimpleName());
            deleteQuery.executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            System.err.println("Failed to clean up entities of type " + entityType.getSimpleName() + ": " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
