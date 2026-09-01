package com.github.hwc2243.service.base;

import com.github.hwc2243.service.ServiceException;
import java.util.List;

public interface EntityService<D, ID> {
	
    public D create(D entity) throws ServiceException;

    public void delete(ID id) throws ServiceException;

    public List<D> findAll () throws ServiceException;
     
    public D get(ID id) throws ServiceException;

    public D update(D entity) throws ServiceException;
}