package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.model.DocumentObjectObjectType;
import com.github.hwc2243.documentlibrary.persistence.DocumentLibraryPersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentFilePersistence;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  @Autowired
  protected DocumentFilePersistence documentFilePersistence;

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

    if (documentFolder.getObjectType() == null) {
      // Folders created before the object-type value was assigned are valid
      // folder rows; repair that legacy value when they are encountered.
      documentFolder.setObjectType(DocumentObjectObjectType.FOLDER);
      documentFolder = documentFolderPersistence.save(documentFolder);
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

    List<DocumentFolderDTO> documentFolders = toDtos(documentFolderPersistence.findByLibraryIdAndParentFolderId(
      libraryId,
      parentFolderId
    ));

    for (DocumentFolderDTO documentFolder : documentFolders) {
      DocumentLibraryDTO library = new DocumentLibraryDTO();
      library.setId(libraryId);
      documentFolder.setLibrary(library);
    }

    return documentFolders;
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

    Deque<Long> folderIds = new ArrayDeque<>();
    Set<Long> visitedFolderIds = new HashSet<>();
    Long currentFolderId = documentFolder.getId();
    Long libraryId = null;

    while (currentFolderId != null) {
      Long folderId = currentFolderId;

      if (!visitedFolderIds.add(folderId)) {
        String message = "A cycle was found in the document folder hierarchy.";

        logger.error(message);

        throw new ServiceException(message);
      }

      folderIds.addFirst(folderId);
      DocumentFolderEntity currentFolder = documentFolderPersistence.findById(folderId)
        .orElseThrow(() -> missingFolder(folderId));

      if (currentFolder.getLibrary() == null || currentFolder.getLibrary().getId() == null) {
        String message = "Document folder " + folderId + " does not belong to a persisted document library.";

        logger.error(message);

        throw new ServiceException(message);
      }

      Long currentLibraryId = currentFolder.getLibrary().getId();

      if (libraryId == null) {
        libraryId = currentLibraryId;
      }
      else if (!libraryId.equals(currentLibraryId)) {
        String message = "All folders in a document folder hierarchy must belong to the same document library.";

        logger.error(message);

        throw new ServiceException(message);
      }

      currentFolderId = currentFolder.getParentFolder() == null
        ? null
        : currentFolder.getParentFolder().getId();
    }

    DocumentLibraryDTO library = new DocumentLibraryDTO();
    library.setId(libraryId);
    Path folderPath = documentLibraryService.getLibraryPath(library);

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
  
  @Override
  public void delete (DocumentFolderDTO folder) throws ServiceException {
	  delete(folder.getId());
  }

  @Override
  public void delete(Long folderId) throws ServiceException {
    if (folderId == null) {
      throw new ServiceException("A document folder ID is required to delete a folder.");
    }

    DocumentFolderDTO documentFolder = get(folderId);

    if (documentFolder == null || documentFolder.getLibrary() == null || documentFolder.getLibrary().getId() == null) {
      throw new ServiceException("A persisted DocumentFolderDTO with a library is required to delete a folder.");
    }

    Long libraryId = documentFolder.getLibrary().getId();

    if (!documentFolderPersistence.findByLibraryIdAndParentFolderId(libraryId, folderId).isEmpty()
      || !documentFilePersistence.findByLibraryIdAndParentFolderId(libraryId, folderId).isEmpty()) {
      throw new ServiceException(
        "Document folder '" + documentFolder.getName() + "' cannot be deleted because it is not empty."
      );
    }

    Path folderPath = getLibraryPath(documentFolder);

    try {
      deleteDirectory(folderPath);
      super.delete(folderId);
    }
    catch (IOException | SecurityException exception) {
      String message = "Unable to delete the document folder directory: " + folderPath;

      logger.error(message, exception);

      throw new ServiceException(message, exception);
    }
  }

  /**
   * A folder is deleted only after its database children and files have been
   * checked. Remove any residual filesystem entries as well, such as entries
   * left by an interrupted earlier operation, so they do not block deletion.
   */
  private void deleteDirectory(Path folderPath) throws IOException {
    if (!Files.exists(folderPath)) {
      return;
    }

    try (Stream<Path> paths = Files.walk(folderPath)) {
      List<Path> entries = paths.sorted(Comparator.reverseOrder()).collect(Collectors.toList());

      for (Path entry : entries) {
        Files.deleteIfExists(entry);
      }
    }
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

    documentFolder.setObjectType(DocumentObjectObjectType.FOLDER);
    entity.setObjectType(DocumentObjectObjectType.FOLDER);
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

  private ServiceException missingFolder(Long folderId) {
    String message = "Document folder not found: " + folderId;

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
