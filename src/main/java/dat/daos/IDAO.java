package dat.daos;

import dat.dtos.GuideDTO;
import dat.exceptions.ApiException;

import java.util.List;

public interface IDAO <T> {

    List<T> getAll() throws ApiException;
    T getById(Long id) throws ApiException;
    T create(T t) throws ApiException;
    void update(Long id, T t) throws ApiException;
    void delete(T t) throws ApiException;
}
