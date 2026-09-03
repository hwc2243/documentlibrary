package com.github.hwc2243.documentlibrary.service.base;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFileEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentFilePersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentFilePersistence;
import com.github.hwc2243.documentlibrary.service.ServiceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDocumentFileServiceImpl<D extends DocumentFileDTO, E extends DocumentFileEntity, ID>
  implements BaseDocumentFileService<D, ID> {

  @Autowired
  private BaseDocumentFilePersistence<E, ID> baseDocumentFilePersistence;
  
  @Autowired
  protected DocumentFilePersistence documentFilePersistence;

    @Override
  public D create (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFilePersistence.save(entity);
    return toDto(saved);
  }
  
  @Override
  public void delete (ID id) throws ServiceException
  {
    baseDocumentFilePersistence.deleteById(id);
  }
  
  @Override
  public List<D> findAll () throws ServiceException
  {
    List<E> entities = baseDocumentFilePersistence.findAll();
    return toDtos(entities);
  }

  @Override
  public D get (ID id) throws ServiceException
  {
    Optional<E> optional = baseDocumentFilePersistence.findById(id);

    return optional.isEmpty() ? null : toDto(optional.get());
  }
  
  @Override
  public D update (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFilePersistence.save(entity);
    return toDto(saved);
  }
  
  protected abstract E toEntity (D dto);
  protected abstract List<E> toEntities (List<D> dtos);

  protected abstract D toDto (E entity);
  protected abstract List<D> toDtos (List<E> entities);
}
