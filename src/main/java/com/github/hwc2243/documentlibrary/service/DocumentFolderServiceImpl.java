package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.model.DocumentObjectObjectType;
import com.github.hwc2243.documentlibrary.persistence.DocumentLibraryPersistence;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFolderServiceImpl
  extends BaseDocumentFolderServiceImpl<DocumentFolderDTO, DocumentFolderEntity,Long>
  implements DocumentFolderService
{
  public static final Logger logger = LoggerFactory.getLogger(DocumentFolderServiceImpl.class);

  
  @Autowired
  protected DocumentFolderMapper documentFolderMapper;

  @Autowired
  protected DocumentLibraryService documentLibraryService;

  @Autowired
  protected DocumentLibraryPersistence documentLibraryPersistence;

  @Override
  public DocumentFolderDTO fetchByName(
    String name,
    DocumentLibraryDTO documentLibrary,
    DocumentFolderDTO parentFolder
  ) throws ServiceException {
    Long libraryId = resolveLibraryId(documentLibrary, parentFolder);
    Long parentFolderId = parentFolder == null ? null : parentFolder.getId();

    DocumentFolderEntity documentFolder = documentFolderPersistence.findFirstByNameAndLibraryIdAndParentFolderId(
      name,
      libraryId,
      parentFolderId
    );

    if (documentFolder == null) {
      return null;
    }

    if (documentFolder.getObjectType() != DocumentObjectObjectType.FOLDER) {
      String message = "Document object named '" + name + "' is not a folder.";

      logger.error(message);

      throw new ServiceException(message);
    }

    return toDto(documentFolder);
  }

  @Override
  public List<DocumentFolderDTO> findFolders(
    DocumentLibraryDTO documentLibrary,
    DocumentFolderDTO parentFolder
  ) throws ServiceException {
    Long libraryId = resolveLibraryId(documentLibrary, parentFolder);
    Long parentFolderId = parentFolder == null ? null : parentFolder.getId();

    return toDtos(documentFolderPersistence.findByLibraryIdAndParentFolderId(
      libraryId,
      parentFolderId
    ));
  }

  private Long resolveLibraryId(DocumentLibraryDTO documentLibrary, DocumentFolderDTO parentFolder)
    throws ServiceException {
    if (documentLibrary == null && parentFolder == null) {
      String message = "A DocumentLibraryDTO or parent DocumentFolderDTO is required to find a folder by name.";

      logger.error(message);

      throw new ServiceException(message);
    }

    if (parentFolder != null && parentFolder.getId() == null) {
      String message = "The parent DocumentFolderDTO must have an ID.";

      logger.error(message);

      throw new ServiceException(message);
    }

    DocumentLibraryDTO parentLibrary = parentFolder == null ? null : parentFolder.getLibrary();

    if (documentLibrary == null) {
      documentLibrary = parentLibrary;
    }

    if (documentLibrary == null || documentLibrary.getId() == null) {
      String message = "A persisted DocumentLibraryDTO is required to find a folder by name.";

      logger.error(message);

      throw new ServiceException(message);
    }

    if (parentFolder != null) {
      if (parentLibrary == null || parentLibrary.getId() == null) {
        String message = "The parent folder must belong to a persisted DocumentLibraryDTO.";

        logger.error(message);

        throw new ServiceException(message);
      }

      if (!documentLibrary.getId().equals(parentLibrary.getId())) {
        String message = "The parent folder does not belong to the specified document library.";

        logger.error(message);

        throw new ServiceException(message);
      }
    }

    return documentLibrary.getId();
  }

  @Override
  public Path getLibraryPath(DocumentFolderDTO documentFolder) throws ServiceException {
    if (documentFolder == null || documentFolder.getId() == null) {
      String message = "A persisted DocumentFolderDTO with an ID is required to determine its library path.";

      logger.error(message);

      throw new ServiceException(message);
    }

    if (documentFolder.getLibrary() == null) {
      String message = "A DocumentLibraryDTO is required to determine a document folder path.";

      logger.error(message);

      throw new ServiceException(message);
    }

    Deque<Long> folderIds = new ArrayDeque<>();
    Set<Long> visitedFolderIds = new HashSet<>();
    DocumentFolderDTO currentFolder = documentFolder;

    while (currentFolder != null) {
      Long folderId = currentFolder.getId();

      if (folderId == null) {
        String message = "Each parent DocumentFolderDTO must have an ID to determine a document folder path.";

        logger.error(message);

        throw new ServiceException(message);
      }

      if (!visitedFolderIds.add(folderId)) {
        String message = "A cycle was found in the document folder hierarchy.";

        logger.error(message);

        throw new ServiceException(message);
      }

      folderIds.addFirst(folderId);
      currentFolder = currentFolder.getParentFolder();
    }

    Path folderPath = documentLibraryService.getLibraryPath(documentFolder.getLibrary());

    for (Long folderId : folderIds) {
      folderPath = folderPath.resolve(folderId.toString());
    }

    return folderPath;
  }

  @Override
  public DocumentFolderDTO create(DocumentFolderDTO documentFolder) throws ServiceException {
    DocumentFolderDTO persistedDocumentFolder = persist(documentFolder);
    documentFolder.setId(persistedDocumentFolder.getId());
    Path folderPath = getLibraryPath(documentFolder);

    try {
      Files.createDirectories(folderPath);
    }
    catch (IOException | SecurityException exception) {
      String message = "Unable to create the document folder directory: " + folderPath;

      logger.error(message, exception);

      throw new ServiceException(message, exception);
    }

    return persistedDocumentFolder;
  }

  @Override
  public DocumentFolderDTO update(DocumentFolderDTO documentFolder) throws ServiceException {
    return persist(documentFolder);
  }

  private DocumentFolderDTO persist(DocumentFolderDTO documentFolder) throws ServiceException {
    if (documentFolder == null || documentFolder.getLibrary() == null
      || documentFolder.getLibrary().getId() == null) {
      String message = "A persisted DocumentLibraryDTO is required to save a document folder.";

      logger.error(message);

      throw new ServiceException(message);
    }

    DocumentLibraryEntity library = documentLibraryPersistence
      .findById(documentFolder.getLibrary().getId())
      .orElseThrow(() -> missingLibrary(documentFolder.getLibrary().getId()));
    DocumentFolderEntity entity = toEntity(documentFolder);

    entity.setLibrary(library);

    if (documentFolder.getParentFolder() != null) {
      if (documentFolder.getParentFolder().getId() == null) {
        String message = "The parent DocumentFolderDTO must have an ID.";

        logger.error(message);

        throw new ServiceException(message);
      }

      DocumentFolderEntity parentFolder = documentFolderPersistence
        .findById(documentFolder.getParentFolder().getId())
        .orElseThrow(() -> missingParentFolder(documentFolder.getParentFolder().getId()));

      if (parentFolder.getLibrary() == null
        || !library.getId().equals(parentFolder.getLibrary().getId())) {
        String message = "The parent folder does not belong to the document library.";

        logger.error(message);

        throw new ServiceException(message);
      }

      entity.setParentFolder(parentFolder);
    }

    DocumentFolderDTO savedFolder = toDto(documentFolderPersistence.save(entity));

    savedFolder.setLibrary(documentFolder.getLibrary());
    savedFolder.setParentFolder(documentFolder.getParentFolder());

    return savedFolder;
  }

  private ServiceException missingLibrary(Long libraryId) {
    String message = "Document library not found: " + libraryId;

    logger.error(message);

    return new ServiceException(message);
  }

  private ServiceException missingParentFolder(Long parentFolderId) {
    String message = "Parent document folder not found: " + parentFolderId;

    logger.error(message);

    return new ServiceException(message);
  }
  
  protected DocumentFolderEntity toEntity (DocumentFolderDTO dto) {
    return documentFolderMapper.toEntity(dto);
  }
  
  protected List<DocumentFolderEntity> toEntities (List<DocumentFolderDTO> dtos) {
    return documentFolderMapper.toEntities(dtos);
  }

  protected DocumentFolderDTO toDto (DocumentFolderEntity entity) {
    if (entity == null) {
      return null;
    }

    DocumentFolderDTO documentFolder = documentFolderMapper.toDto(entity);

    if (entity.getLibrary() != null) {
      DocumentLibraryDTO library = new DocumentLibraryDTO();
      library.setId(entity.getLibrary().getId());
      documentFolder.setLibrary(library);
    }

    if (entity.getParentFolder() != null) {
      DocumentFolderDTO parentFolder = new DocumentFolderDTO();
      parentFolder.setId(entity.getParentFolder().getId());
      documentFolder.setParentFolder(parentFolder);
    }

    return documentFolder;
  }
  
  protected List<DocumentFolderDTO> toDtos (List<DocumentFolderEntity> entities) {
    List<DocumentFolderDTO> documentFolders = new ArrayList<>();

    for (DocumentFolderEntity entity : entities) {
      documentFolders.add(toDto(entity));
    }

    return documentFolders;
  }
}
