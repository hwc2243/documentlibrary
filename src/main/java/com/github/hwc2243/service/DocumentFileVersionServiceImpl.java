package com.github.hwc2243.service;

import com.github.hwc2243.dto.DocumentFileVersionDTO;
import com.github.hwc2243.entity.DocumentFileVersionEntity;
import com.github.hwc2243.service.base.BaseDocumentFileVersionServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileVersionServiceImpl
  extends BaseDocumentFileVersionServiceImpl<DocumentFileVersionDTO, DocumentFileVersionEntity,Long>
  implements DocumentFileVersionService
{
  
  @Autowired
  protected DocumentFileVersionMapper documentFileVersionMapper;
  
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