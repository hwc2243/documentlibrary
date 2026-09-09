package com.github.hwc2243.documentlibrary.service.base;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentLibraryPersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentLibraryPersistence;
import com.github.hwc2243.documentlibrary.service.ServiceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDocumentLibraryServiceImpl<D extends DocumentLibraryDTO, E extends DocumentLibraryEntity, ID>
  implements BaseDocumentLibraryService<D, ID> {

  @Autowired
  private BaseDocumentLibraryPersistence<E, ID> baseDocumentLibraryPersistence;
  
  @Autowired
  protected DocumentLibraryPersistence documentLibraryPersistence;

    @Override
  public D create (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentLibraryPersistence.save(entity);
    return toDto(saved);
  }
  
  @Override
  public void delete (ID id) throws ServiceException
  {
    baseDocumentLibraryPersistence.deleteById(id);
  }
  
  @Override
  public List<D> findAll () throws ServiceException
  {
    List<E> entities = baseDocumentLibraryPersistence.findAll();
    return toDtos(entities);
  }

  @Override
  public D fetchByName (String name)
  {
	return toDto(baseDocumentLibraryPersistence.findFirstByName(name));
  }

  @Override
  public D get (ID id) throws ServiceException
  {
    Optional<E> optional = baseDocumentLibraryPersistence.findById(id);

    return optional.isEmpty() ? null : toDto(optional.get());
  }
  
  @Override
  public D update (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentLibraryPersistence.save(entity);
    return toDto(saved);
  }
  
  protected abstract E toEntity (D dto);
  protected abstract List<E> toEntities (List<D> dtos);

  protected abstract D toDto (E entity);
  protected abstract List<D> toDtos (List<E> entities);
}
