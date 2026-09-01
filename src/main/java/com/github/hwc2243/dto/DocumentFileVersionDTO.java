package com.github.hwc2243.dto;

import com.github.hwc2243.dto.base.BaseDocumentFileVersionDTO;
import com.github.hwc2243.dto.DocumentFileDTO;

public class DocumentFileVersionDTO extends BaseDocumentFileVersionDTO
{
  private static final long serialVersionUID = 1L;

  public DocumentFileVersionDTO () {
  }
  
  public DocumentFileVersionDTO (Builder builder) {
    super(builder);
  }
  
  public static class Builder extends BaseDocumentFileVersionDTO.Builder {
    public DocumentFileVersionDTO build() {
      return new DocumentFileVersionDTO(this);
    }
  }
}