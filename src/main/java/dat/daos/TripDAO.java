package dat.daos;

import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO>, ITripGuideDAO {

    private EntityManagerFactory emf;

    public TripDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<TripDTO> getAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {

            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
            return query.getResultList().stream().map(TripDTO::new).collect(Collectors.toList());

        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to get trips");
        }
    }

    @Override
    public TripDTO getById(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {

            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            return new TripDTO(trip);
        }
    }

    public TripDTO create(TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {

            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO);

            em.persist(trip);

            em.getTransaction().commit();

            return new TripDTO(trip);
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to create trip");
        }
    }


    @Override
    public void update(Long id, TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }

            if (tripDTO.getStarttime() != null) {
                trip.setStarttime(tripDTO.getStarttime());
            }
            if (tripDTO.getEndtime() != null) {
                trip.setEndtime(tripDTO.getEndtime());
            }
            if (tripDTO.getStartposition() != null) {
                trip.setStartposition(tripDTO.getStartposition());
            }
            if (tripDTO.getName() != null) {
                trip.setName(tripDTO.getName());
            }
            trip.setPrice(tripDTO.getPrice());
            if (tripDTO.getCategory() != null) {
                trip.setCategory(tripDTO.getCategory());
            }

            em.merge(trip);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to update trip");
        }
    }

    @Override
    public void delete(TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripDTO.getId());
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            em.remove(trip);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to delete trip");
        }
    }

    @Override
    public void addGuideToTrip(Long tripId, Long guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);
            if (trip == null || guide == null) {
                throw new ApiException(404, "Trip or Guide not found");
            }
            trip.setGuide(guide);
            em.merge(trip);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to add guide to trip");
        }
    }

    public List<Map<String, Object>> getTotalPriceByGuide() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            List<Object[]> results = em.createQuery(
                            "SELECT g.id, SUM(t.price) FROM Guide g JOIN g.trips t GROUP BY g.id", Object[].class)
                    .getResultList();

            return results.stream()
                    .map(result -> Map.of("guideId", result[0], "totalPrice", result[1]))
                    .collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to calculate total prices by guides");
        }
    }
}