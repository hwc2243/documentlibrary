package com.github.hwc2243.service;

import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.service.base.BaseDocumentLibraryServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentLibraryServiceImpl
  extends BaseDocumentLibraryServiceImpl<DocumentLibraryDTO, DocumentLibraryEntity,Long>
  implements DocumentLibraryService
{
  
  @Autowired
  protected DocumentLibraryMapper documentLibraryMapper;
  
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