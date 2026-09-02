package com.github.hwc2243.documentlibrary.dto;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentObjectDTO;

public class DocumentLibraryDTO extends BaseDocumentLibraryDTO
{
  private static final long serialVersionUID = 1L;

  public DocumentLibraryDTO () {
  }
  
  public DocumentLibraryDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseDocumentLibraryDTO.Builder {
    public DocumentLibraryDTO build() {
      return new DocumentLibraryDTO(this);
    }
  }
}