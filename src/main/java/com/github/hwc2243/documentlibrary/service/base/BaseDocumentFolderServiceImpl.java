package com.github.hwc2243.documentlibrary.service.base;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentFolderPersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentFolderPersistence;
import com.github.hwc2243.documentlibrary.service.ServiceException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseDocumentFolderServiceImpl<D extends DocumentFolderDTO, E extends DocumentFolderEntity, ID>
  implements BaseDocumentFolderService<D, ID> {

  @Autowired
  private BaseDocumentFolderPersistence<E, ID> baseDocumentFolderPersistence;

  @Autowired
  protected DocumentFolderPersistence documentFolderPersistence;

    @Override
  public D create (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFolderPersistence.save(entity);
    return toDto(saved);
  }
  
  @Override
  public void delete (ID id) throws ServiceException
  {
    baseDocumentFolderPersistence.deleteById(id);
  }
  
  @Override
  public List<D> findAll () throws ServiceException
  {
    List<E> entities = baseDocumentFolderPersistence.findAll();
    return toDtos(entities);
  }

  @Override
  public D fetchByNameAndLibraryIdAndParentFolderId (String name, Long libraryId, Long parentFolderId)
  {
	return toDto(baseDocumentFolderPersistence.findFirstByNameAndLibraryIdAndParentFolderId(name, libraryId, parentFolderId));
  }

  @Override
  public List<D> findByLibraryIdAndParentFolderId (Long libraryId, Long parentFolderId)
  {
	return toDtos(baseDocumentFolderPersistence.findByLibraryIdAndParentFolderId(libraryId, parentFolderId));
  }


  @Override
  public D get (ID id) throws ServiceException
  {
    Optional<E> optional = baseDocumentFolderPersistence.findById(id);

    return optional.isEmpty() ? null : toDto(optional.get());
  }
  
  @Override
  public D update (D dto) throws ServiceException
  {
    E entity = toEntity(dto);
    E saved = baseDocumentFolderPersistence.save(entity);
    return toDto(saved);
  }
  
  protected abstract E toEntity (D dto);
  protected abstract List<E> toEntities (List<D> dtos);

  protected abstract D toDto (E entity);
  protected abstract List<D> toDtos (List<E> entities);
}
