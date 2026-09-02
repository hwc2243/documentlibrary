package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentLibraryServiceImpl;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DocumentLibraryServiceImpl
  extends BaseDocumentLibraryServiceImpl<DocumentLibraryDTO, DocumentLibraryEntity,Long>
  implements DocumentLibraryService
{
  public static final Logger logger = LoggerFactory.getLogger(DocumentLibraryServiceImpl.class);

  @Value("${documentlibrary.path:#{null}}")
  protected String dlPath;
  
  @Autowired
  protected DocumentLibraryMapper documentLibraryMapper;

  @PostConstruct
  protected void validateDocumentLibraryPath() {
    if (dlPath == null || dlPath.isBlank()) {
      String message = "The documentlibrary.path property must be configured.";

      logger.error(message);

      throw new IllegalStateException(message);
    }

    try {
      Path documentLibraryPath = Path.of(dlPath);

      if (Files.exists(documentLibraryPath) && !Files.isDirectory(documentLibraryPath)) {
        String message = "The documentlibrary.path value is not a directory: " + dlPath;

        logger.error(message);

        throw new IllegalStateException(message);
      }

      if (!Files.exists(documentLibraryPath)) {
        Files.createDirectories(documentLibraryPath);
      }
    }
    catch (IOException | InvalidPathException | SecurityException exception) {
      String message = "Unable to create the document library directory: " + dlPath;

      logger.error(message, exception);

      throw new IllegalStateException(message, exception);
    }
  }

  protected DocumentLibraryEntity toEntity (DocumentLibraryDTO dto) {
    return documentLibraryMapper.toEntity(dto);
  }
  
  protected List<DocumentLibraryEntity> toEntities (List<DocumentLibraryDTO> dtos) {
    return documentLibraryMapper.toEntities(dtos);
  }

  protected DocumentLibraryDTO toDto (DocumentLibraryEntity entity) {
    return documentLibraryMapper.toDto(entity);
  }
  
  protected List<DocumentLibraryDTO> toDtos (List<DocumentLibraryEntity> entities) {
    return documentLibraryMapper.toDtos(entities);
  }
}
