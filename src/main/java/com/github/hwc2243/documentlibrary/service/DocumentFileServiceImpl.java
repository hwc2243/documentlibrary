package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFileServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentFileServiceImpl
  extends BaseDocumentFileServiceImpl<DocumentFileDTO, DocumentFileEntity,Long>
  implements DocumentFileService
{
  
  @Autowired
  protected DocumentFileMapper documentFileMapper;
  
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