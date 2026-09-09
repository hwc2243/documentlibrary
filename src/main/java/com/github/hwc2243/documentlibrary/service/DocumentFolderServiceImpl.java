package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.model.DocumentObjectObjectType;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderServiceImpl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

  @Override
  public DocumentFolderDTO fetchByName(String name) throws ServiceException {
    DocumentFolderEntity documentFolder = documentFolderPersistence.findFirstByName(name);

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
    DocumentFolderDTO persistedDocumentFolder = super.create(documentFolder);
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
  
  protected DocumentFolderEntity toEntity (DocumentFolderDTO dto) {
    return documentFolderMapper.toEntity(dto);
  }
  
  protected List<DocumentFolderEntity> toEntities (List<DocumentFolderDTO> dtos) {
    return documentFolderMapper.toEntities(dtos);
  }

  protected DocumentFolderDTO toDto (DocumentFolderEntity entity) {
    return documentFolderMapper.toDto(entity);
  }
  
  protected List<DocumentFolderDTO> toDtos (List<DocumentFolderEntity> entities) {
    return documentFolderMapper.toDtos(entities);
  }
}
