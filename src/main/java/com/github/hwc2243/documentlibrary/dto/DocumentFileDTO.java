package com.github.hwc2243.documentlibrary.dto;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;

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