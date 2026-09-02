package com.github.hwc2243.documentlibrary.service;



import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentObjectDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentObjectEntity;
import java.util.List;
import java.util.Set;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentFolderMapper {

  @Named("documentFolderDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  @Mapping(target = "children", ignore = true)
  DocumentFolderDTO toDto(DocumentFolderEntity entity);

  @Named("documentFolderShallow")
  @Mapping(
      target = "children",
      source = "children",
      qualifiedByName = "documentObjectDefault"
  )
  DocumentFolderDTO toDtoShallow(DocumentFolderEntity entity);


  @Named("documentObjectDefault")
  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  DocumentObjectDTO documentObjectToDto(
      DocumentObjectEntity entity
  );


  @IterableMapping(qualifiedByName = "documentObjectDefault")
  List<DocumentObjectDTO> childrenToDtos(
      List<DocumentObjectEntity> entities
  );


  @Mapping(target = "library", ignore = true)
  @Mapping(target = "parentFolder", ignore = true)
  @Mapping(target = "children", ignore = true)
  DocumentFolderEntity toEntity(DocumentFolderDTO dto);

  @IterableMapping(qualifiedByName = "documentFolderDefault")
  List<DocumentFolderDTO> toDtos(List<DocumentFolderEntity> entities);

  @IterableMapping(qualifiedByName = "documentFolderShallow")
  List<DocumentFolderDTO> toDtosShallow(List<DocumentFolderEntity> entities);

  List<DocumentFolderEntity> toEntities(List<DocumentFolderDTO> dtos);
}