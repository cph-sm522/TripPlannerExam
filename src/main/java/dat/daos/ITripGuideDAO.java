package dat.daos;

import dat.exceptions.ApiException;


public interface ITripGuideDAO {
    void addGuideToTrip(Long tripId, Long guideId) throws ApiException;
}
