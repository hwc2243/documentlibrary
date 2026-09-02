package com.github.hwc2243.documentlibrary.dto;

import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentObjectDTO;

public class DocumentFolderDTO extends BaseDocumentFolderDTO
{
  private static final long serialVersionUID = 1L;

  public DocumentFolderDTO () {
  }
  
  public DocumentFolderDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseDocumentFolderDTO.Builder {
    public DocumentFolderDTO build() {
      return new DocumentFolderDTO(this);
    }
  }
}