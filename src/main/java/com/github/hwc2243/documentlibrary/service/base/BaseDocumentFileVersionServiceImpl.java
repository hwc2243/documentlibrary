package com.github.hwc2243.documentlibrary.service.base;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentFileVersionPersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentFileVersionPersistence;
import com.github.hwc2243.documentlibrary.service.ServiceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDocumentFileVersionServiceImpl<D extends DocumentFileVersionDTO, E extends DocumentFileVersionEntity, ID>
  implements BaseDocumentFileVersionService<D, ID> {

  @Autowired
  private BaseDocumentFileVersionPersistence<E, ID> baseDocumentFileVersionPersistence;
  
  @Autowired
  protected DocumentFileVersionPersistence documentFileVersionPersistence;

    @Override
  public D create (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFileVersionPersistence.save(entity);
    return toDto(saved);
  }
  
  @Override
  public void delete (ID id) throws ServiceException
  {
    baseDocumentFileVersionPersistence.deleteById(id);
  }
  
  @Override
  public List<D> findAll () throws ServiceException
  {
    List<E> entities = baseDocumentFileVersionPersistence.findAll();
    return toDtos(entities);
  }

  @Override
  public D get (ID id) throws ServiceException
  {
    Optional<E> optional = baseDocumentFileVersionPersistence.findById(id);

    return optional.isEmpty() ? null : toDto(optional.get());
  }
  
  @Override
  public D update (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFileVersionPersistence.save(entity);
    return toDto(saved);
  }
  
  protected abstract E toEntity (D dto);
  protected abstract List<E> toEntities (List<D> dtos);

  protected abstract D toDto (E entity);
  protected abstract List<D> toDtos (List<E> entities);
}
