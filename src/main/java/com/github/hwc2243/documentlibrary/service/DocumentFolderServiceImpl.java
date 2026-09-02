package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderServiceImpl;
import java.util.List;
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
