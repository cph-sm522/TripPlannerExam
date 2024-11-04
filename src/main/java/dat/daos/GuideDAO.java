package dat.daos;

import dat.dtos.GuideDTO;
import dat.entities.Guide;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class GuideDAO implements IDAO<GuideDTO> {

    private EntityManagerFactory emf;

    public GuideDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<GuideDTO> getAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g", Guide.class);
            return query.getResultList().stream().map(GuideDTO::new).collect(Collectors.toList());
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to fetch guides");
        }
    }

    @Override
    public GuideDTO getById(Long id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            return new GuideDTO(guide);
        }
    }

    @Override
    public GuideDTO create(GuideDTO guideDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = new Guide(guideDTO);
            em.persist(guide);
            em.getTransaction().commit();
            return new GuideDTO(guide);
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to create guide");
        }
    }

    @Override
    public void update(Long id, GuideDTO guideDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Guide guide = em.find(Guide.class, id);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }

            if (guideDTO.getFirstname() != null) {
                guide.setFirstname(guideDTO.getFirstname());
            }
            if (guideDTO.getLastname() != null) {
                guide.setLastname(guideDTO.getLastname());
            }
            if (guideDTO.getEmail() != null) {
                guide.setEmail(guideDTO.getEmail());
            }
            if (guideDTO.getPhone() != null) {
                guide.setPhone(guideDTO.getPhone());
            }
            guide.setYearsOfExperience(guideDTO.getYearsOfExperience());

            em.merge(guide);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Unable to update guide");
        }
    }


    @Override
    public void delete(GuideDTO guideDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, guideDTO.getId());
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            em.remove(guide);
            em.getTransaction().commit();
        }
    }
}

