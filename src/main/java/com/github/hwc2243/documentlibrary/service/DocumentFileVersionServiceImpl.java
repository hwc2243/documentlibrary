package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFileVersionServiceImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileVersionServiceImpl
  extends BaseDocumentFileVersionServiceImpl<DocumentFileVersionDTO, DocumentFileVersionEntity,Long>
  implements DocumentFileVersionService
{
  public static final Logger logger = LoggerFactory.getLogger(DocumentFileVersionServiceImpl.class);

  
  @Autowired
  protected DocumentFileVersionMapper documentFileVersionMapper;

  @Override
  public List<DocumentFileVersionDTO> findVersions(DocumentFileDTO documentFile)
    throws ServiceException {
    if (documentFile == null || documentFile.getId() == null) {
      String message = "A persisted DocumentFileDTO with an ID is required to find document file versions.";

      logger.error(message);

      throw new ServiceException(message);
    }

    List<DocumentFileVersionDTO> versions = toDtos(
      documentFileVersionPersistence.findByDocumentFileId(documentFile.getId())
    );

    for (DocumentFileVersionDTO version : versions) {
      version.setDocumentFile(documentFile);
    }

    return versions;
  }
  
  protected DocumentFileVersionEntity toEntity (DocumentFileVersionDTO dto) {
    return documentFileVersionMapper.toEntity(dto);
  }
  
  protected List<DocumentFileVersionEntity> toEntities (List<DocumentFileVersionDTO> dtos) {
    return documentFileVersionMapper.toEntities(dtos);
  }

  protected DocumentFileVersionDTO toDto (DocumentFileVersionEntity entity) {
    return documentFileVersionMapper.toDto(entity);
  }
  
  protected List<DocumentFileVersionDTO> toDtos (List<DocumentFileVersionEntity> entities) {
    return documentFileVersionMapper.toDtos(entities);
  }
}
