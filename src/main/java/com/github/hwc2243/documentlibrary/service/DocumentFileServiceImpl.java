package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.model.DocumentObjectObjectType;
import com.github.hwc2243.documentlibrary.persistence.DocumentFileVersionPersistence;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFileServiceImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileServiceImpl
  extends BaseDocumentFileServiceImpl<DocumentFileDTO, DocumentFileEntity,Long>
  implements DocumentFileService
{
  public static final Logger logger = LoggerFactory.getLogger(DocumentFileServiceImpl.class);

  
  @Autowired
  protected DocumentFileMapper documentFileMapper;

  @Autowired
  protected DocumentLibraryService documentLibraryService;

  @Autowired
  protected DocumentFolderService documentFolderService;

  @Autowired
  protected DocumentFileVersionPersistence documentFileVersionPersistence;

  @Override
  public DocumentFileDTO fetchByName(String name) throws ServiceException {
    DocumentFileEntity documentFile = documentFilePersistence.findFirstByName(name);

    if (documentFile == null) {
      return null;
    }

    if (documentFile.getObjectType() != DocumentObjectObjectType.FILE) {
      String message = "Document object named '" + name + "' is not a file.";

      logger.error(message);

      throw new ServiceException(message);
    }

    return toDto(documentFile);
  }

  @Override
  public Path getLibraryPath(DocumentFileDTO documentFile) throws ServiceException {
    if (documentFile == null || documentFile.getId() == null) {
      String message = "A persisted DocumentFileDTO with an ID is required to determine its library path.";

      logger.error(message);

      throw new ServiceException(message);
    }

    if (documentFile.getLibrary() == null) {
      String message = "A DocumentLibraryDTO is required to determine a document file path.";

      logger.error(message);

      throw new ServiceException(message);
    }

    Path parentPath = documentFile.getParentFolder() == null
      ? documentLibraryService.getLibraryPath(documentFile.getLibrary())
      : documentFolderService.getLibraryPath(documentFile.getParentFolder());

    return parentPath.resolve(documentFile.getId().toString());
  }

  @Override
  public DocumentFileVersionDTO store (DocumentFileDTO documentFile, byte[] content) throws ServiceException {
    if (content == null) {
      String message = "Document file content must not be null.";

      logger.error(message);

      throw new ServiceException(message);
    }

    return store(documentFile, new ByteArrayInputStream(content));
  }

  @Override
  public DocumentFileVersionDTO store (DocumentFileDTO documentFile, InputStream content) throws ServiceException {
    if (content == null) {
      String message = "Document file content must not be null.";

      logger.error(message);

      throw new ServiceException(message);
    }

    DocumentFileDTO persistedDocumentFile = ensurePersistedDocumentFile(documentFile);
    Path fileDirectory = getLibraryPath(documentFile);
    DocumentFileVersionEntity savedVersion = createVersion(persistedDocumentFile);
    Path versionPath = fileDirectory.resolve(savedVersion.getId().toString());

    try {
      Files.createDirectories(fileDirectory);
      long size = Files.copy(content, versionPath, StandardCopyOption.REPLACE_EXISTING);

      savedVersion.setSize(size);
      savedVersion = documentFileVersionPersistence.save(savedVersion);

      return toDto(savedVersion, persistedDocumentFile);
    }
    catch (IOException | SecurityException exception) {
      String message = "Unable to store document file version at: " + versionPath;

      logger.error(message, exception);
      documentFileVersionPersistence.deleteById(savedVersion.getId());

      throw new ServiceException(message, exception);
    }
  }

  @Override
  public InputStream load(DocumentFileDTO documentFile) throws ServiceException {
    DocumentFileVersionEntity latestVersion = documentFileVersionPersistence
      .findTopByDocumentFile_IdOrderByVersionDesc(requireDocumentFileId(documentFile))
      .orElseThrow(() -> fileNotFound(documentFile));

    if (latestVersion.getId() == null) {
      String message = "The latest document file version does not have an ID.";

      logger.error(message);

      throw new ServiceException(message);
    }

    Path versionPath = getLibraryPath(documentFile).resolve(latestVersion.getId().toString());

    if (!Files.isRegularFile(versionPath)) {
      String message = "The stored document file version does not exist: " + versionPath;

      logger.error(message);

      throw new ServiceException(message);
    }

    try {
      return Files.newInputStream(versionPath);
    }
    catch (IOException | SecurityException exception) {
      String message = "Unable to load document file version: " + versionPath;

      logger.error(message, exception);

      throw new ServiceException(message, exception);
    }
  }

  private Long requireDocumentFileId(DocumentFileDTO documentFile) throws ServiceException {
    if (documentFile == null || documentFile.getId() == null) {
      String message = "A persisted DocumentFileDTO with an ID is required to load content.";

      logger.error(message);

      throw new ServiceException(message);
    }

    return documentFile.getId();
  }

  private ServiceException fileNotFound(DocumentFileDTO documentFile) {
    String message = "No stored versions exist for document file ID: " + documentFile.getId();

    logger.error(message);

    return new ServiceException(message);
  }

  private DocumentFileDTO ensurePersistedDocumentFile(DocumentFileDTO documentFile) throws ServiceException {
    if (documentFile == null) {
      String message = "A DocumentFileDTO is required to store content.";

      logger.error(message);

      throw new ServiceException(message);
    }

    if (documentFile.getId() != null) {
      return documentFile;
    }

    DocumentFileDTO persistedDocumentFile = super.create(documentFile);
    documentFile.setId(persistedDocumentFile.getId());

    return persistedDocumentFile;
  }

  private DocumentFileVersionEntity createVersion(DocumentFileDTO documentFile) {
    Long nextVersion = documentFileVersionPersistence
      .findTopByDocumentFile_IdOrderByVersionDesc(documentFile.getId())
      .map(version -> version.getVersion() + 1)
      .orElse(1L);
    DocumentFileVersionEntity documentFileVersion = new DocumentFileVersionEntity();

    documentFileVersion.setDocumentFile(documentFilePersistence.getReferenceById(documentFile.getId()));
    documentFileVersion.setVersion(nextVersion);
    documentFileVersion.setMimeType(documentFile.getMimeType());
    documentFileVersion.setCreateDate(LocalDateTime.now());

    return documentFileVersionPersistence.save(documentFileVersion);
  }

  private DocumentFileVersionDTO toDto(
    DocumentFileVersionEntity documentFileVersion,
    DocumentFileDTO documentFile
  ) {
    DocumentFileVersionDTO dto = new DocumentFileVersionDTO();

    dto.setId(documentFileVersion.getId());
    dto.setVersion(documentFileVersion.getVersion());
    dto.setSize(documentFileVersion.getSize());
    dto.setMimeType(documentFileVersion.getMimeType());
    dto.setCreateDate(documentFileVersion.getCreateDate());
    dto.setDocumentFile(documentFile);

    return dto;
  }
  
  protected DocumentFileEntity toEntity (DocumentFileDTO dto) {
    return documentFileMapper.toEntity(dto);
  }
  
  protected List<DocumentFileEntity> toEntities (List<DocumentFileDTO> dtos) {
    return documentFileMapper.toEntities(dtos);
  }

  protected DocumentFileDTO toDto (DocumentFileEntity entity) {
    return documentFileMapper.toDto(entity);
  }
  
  protected List<DocumentFileDTO> toDtos (List<DocumentFileEntity> entities) {
    return documentFileMapper.toDtos(entities);
  }
}
