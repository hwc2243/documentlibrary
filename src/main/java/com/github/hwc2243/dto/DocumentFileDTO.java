package com.github.hwc2243.dto;

import com.github.hwc2243.dto.base.BaseDocumentFileDTO;
import com.github.hwc2243.dto.DocumentFileVersionDTO;
import com.github.hwc2243.dto.DocumentFolderDTO;
import com.github.hwc2243.dto.DocumentLibraryDTO;

public class DocumentFileDTO extends BaseDocumentFileDTO
{
  private static final long serialVersionUID = 1L;

  public DocumentFileDTO () {
  }
  
  public DocumentFileDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseDocumentFileDTO.Builder {
    public DocumentFileDTO build() {
      return new DocumentFileDTO(this);
    }
  }
}