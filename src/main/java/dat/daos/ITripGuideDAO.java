package dat.daos;

import dat.dtos.TripDTO;
import dat.exceptions.ApiException;
import java.util.Set;

public interface ITripGuideDAO {
    void addGuideToTrip(Long tripId, Long guideId) throws ApiException;
    Set<TripDTO> getTripsByGuide(Long guideId) throws ApiException;
}
