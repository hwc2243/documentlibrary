package com.github.hwc2243.documentlibrary.service;



import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentFileVersionMapper {

  @Named("documentFileVersionDefault")
  @Mapping(target = "documentFile", ignore = true)
  DocumentFileVersionDTO toDto(DocumentFileVersionEntity entity);

  @Named("documentFileVersionShallow")
  @Mapping(
      target = "documentFile",
      source = "documentFile",
      qualifiedByName = "documentFileDefault"
  )
  DocumentFileVersionDTO toDtoShallow(DocumentFileVersionEntity entity);


  @Named("documentFileDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  @Mapping(target = "versions", ignore = true)
  DocumentFileDTO documentFileToDto(
      DocumentFileEntity entity
  );


  @IterableMapping(qualifiedByName = "documentFileDefault")
  List<DocumentFileDTO> documentFileToDtos(
      List<DocumentFileEntity> entities
  );


  @Mapping(target = "documentFile", ignore = true)
  DocumentFileVersionEntity toEntity(DocumentFileVersionDTO dto);

  @IterableMapping(qualifiedByName = "documentFileVersionDefault")
  List<DocumentFileVersionDTO> toDtos(List<DocumentFileVersionEntity> entities);

  @IterableMapping(qualifiedByName = "documentFileVersionShallow")
  List<DocumentFileVersionDTO> toDtosShallow(List<DocumentFileVersionEntity> entities);

  List<DocumentFileVersionEntity> toEntities(List<DocumentFileVersionDTO> dtos);
}