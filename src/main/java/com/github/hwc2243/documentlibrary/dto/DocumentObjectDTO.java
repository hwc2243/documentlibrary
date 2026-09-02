package com.github.hwc2243.documentlibrary.dto;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentObjectDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;

public class DocumentObjectDTO extends BaseDocumentObjectDTO
{
  private static final long serialVersionUID = 1L;

  public DocumentObjectDTO () {
  }
  
  public DocumentObjectDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseDocumentObjectDTO.Builder {
    public DocumentObjectDTO build() {
      return new DocumentObjectDTO(this);
    }
  }
}