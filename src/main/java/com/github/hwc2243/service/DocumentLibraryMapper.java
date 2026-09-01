package com.github.hwc2243.service;



import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.dto.DocumentObjectDTO;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.entity.DocumentObjectEntity;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentLibraryMapper {

  @Named("documentLibraryDefault")
  @Mapping(target = "objects", ignore = true)
  DocumentLibraryDTO toDto(DocumentLibraryEntity entity);

  @Named("documentLibraryShallow")
  @Mapping(
      target = "objects",
      source = "objects",
      qualifiedByName = "documentObjectDefault"
  )
  DocumentLibraryDTO toDtoShallow(DocumentLibraryEntity entity);


  @Named("documentObjectDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentObjectDTO documentObjectToDto(
      DocumentObjectEntity entity
  );


  @IterableMapping(qualifiedByName = "documentObjectDefault")
  List<DocumentObjectDTO> objectsToDtos(
      List<DocumentObjectEntity> entities
  );


  @Mapping(target = "objects", ignore = true)
  DocumentLibraryEntity toEntity(DocumentLibraryDTO dto);

  @IterableMapping(qualifiedByName = "documentLibraryDefault")
  List<DocumentLibraryDTO> toDtos(List<DocumentLibraryEntity> entities);

  @IterableMapping(qualifiedByName = "documentLibraryShallow")
  List<DocumentLibraryDTO> toDtosShallow(List<DocumentLibraryEntity> entities);

  List<DocumentLibraryEntity> toEntities(List<DocumentLibraryDTO> dtos);
}