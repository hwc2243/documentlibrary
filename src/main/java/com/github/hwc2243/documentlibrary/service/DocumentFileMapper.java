package com.github.hwc2243.documentlibrary.service;



import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentFileMapper {

  @Named("documentFileDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentFileDTO toDto(DocumentFileEntity entity);

  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentFileEntity toEntity(DocumentFileDTO dto);

  @IterableMapping(qualifiedByName = "documentFileDefault")
  List<DocumentFileDTO> toDtos(List<DocumentFileEntity> entities);

  List<DocumentFileEntity> toEntities(List<DocumentFileDTO> dtos);
}
