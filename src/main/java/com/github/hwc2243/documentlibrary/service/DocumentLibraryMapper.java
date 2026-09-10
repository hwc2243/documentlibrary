package com.github.hwc2243.documentlibrary.service;



import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentLibraryMapper {

  @Named("documentLibraryDefault")
  DocumentLibraryDTO toDto(DocumentLibraryEntity entity);

  DocumentLibraryEntity toEntity(DocumentLibraryDTO dto);

  @IterableMapping(qualifiedByName = "documentLibraryDefault")
  List<DocumentLibraryDTO> toDtos(List<DocumentLibraryEntity> entities);

  List<DocumentLibraryEntity> toEntities(List<DocumentLibraryDTO> dtos);
}
