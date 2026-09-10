package com.github.hwc2243.documentlibrary.service;



import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentFolderMapper {

  @Named("documentFolderDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentFolderDTO toDto(DocumentFolderEntity entity);

  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentFolderEntity toEntity(DocumentFolderDTO dto);

  @IterableMapping(qualifiedByName = "documentFolderDefault")
  List<DocumentFolderDTO> toDtos(List<DocumentFolderEntity> entities);

  List<DocumentFolderEntity> toEntities(List<DocumentFolderDTO> dtos);
}
