package com.github.hwc2243.dto;

import com.github.hwc2243.dto.base.BaseDocumentObjectDTO;
import com.github.hwc2243.dto.DocumentFolderDTO;
import com.github.hwc2243.dto.DocumentLibraryDTO;

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