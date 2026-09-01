package com.github.hwc2243.service;



import com.github.hwc2243.dto.DocumentFileDTO;
import com.github.hwc2243.dto.DocumentFileVersionDTO;
import com.github.hwc2243.dto.DocumentFolderDTO;
import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.entity.DocumentFileEntity;
import com.github.hwc2243.entity.DocumentFileVersionEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentFileMapper {

  @Named("documentFileDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  @Mapping(target = "versions", ignore = true)
  DocumentFileDTO toDto(DocumentFileEntity entity);

  @Named("documentFileShallow")
  @Mapping(
      target = "versions",
      source = "versions",
      qualifiedByName = "documentFileVersionDefault"
  )
  DocumentFileDTO toDtoShallow(DocumentFileEntity entity);


  @Named("documentFileVersionDefault")
  @Mapping(target = "documentFile", ignore = true)
  DocumentFileVersionDTO documentFileVersionToDto(
      DocumentFileVersionEntity entity
  );


  @IterableMapping(qualifiedByName = "documentFileVersionDefault")
  List<DocumentFileVersionDTO> versionsToDtos(
      List<DocumentFileVersionEntity> entities
  );


  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  @Mapping(target = "versions", ignore = true)
  DocumentFileEntity toEntity(DocumentFileDTO dto);

  @IterableMapping(qualifiedByName = "documentFileDefault")
  List<DocumentFileDTO> toDtos(List<DocumentFileEntity> entities);

  @IterableMapping(qualifiedByName = "documentFileShallow")
  List<DocumentFileDTO> toDtosShallow(List<DocumentFileEntity> entities);

  List<DocumentFileEntity> toEntities(List<DocumentFileDTO> dtos);
}