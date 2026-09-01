package com.github.hwc2243.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.dto.base.BaseDocumentObjectDTO;
import com.github.hwc2243.dto.DocumentFileDTO;
import com.github.hwc2243.dto.DocumentFileVersionDTO;
import com.github.hwc2243.dto.DocumentFolderDTO;
import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.model.base.BaseDocumentFile;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentFileDTO
extends BaseDocumentObjectDTO
implements BaseDocumentFile<DocumentLibraryDTO, DocumentFolderDTO, DocumentFileVersionDTO>,  Serializable
{
  protected Long id = null;

  protected Long size = null;
  
  protected String mimeType = null;
  
  protected List<DocumentFileVersionDTO> versions;


  protected BaseDocumentFileDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentFileDTO (Builder builder)
  {
    this.id = builder.id;
    this.size = builder.size;
    this.mimeType = builder.mimeType;
    this.versions = builder.versions;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }


  public Long getSize ()
  {
    return this.size;
  }
  
  public void setSize (Long size)
  {
    this.size = size;
  }
  

  public String getMimeType ()
  {
    return this.mimeType;
  }
  
  public void setMimeType (String mimeType)
  {
    this.mimeType = mimeType;
  }
  

  public List<DocumentFileVersionDTO> getVersions ()
  {
    return this.versions;
  }
  
  public void setVersions (List<DocumentFileVersionDTO> versions)
  {
    this.versions = versions;
  }


    @Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
			
		BaseDocumentFileDTO other = (BaseDocumentFileDTO) obj;
		return id == other.id;
	}

  public abstract static class Builder {

  private Long id = null;

  private Long size = null;
  
  private String mimeType = null;
  

    private List<DocumentFileVersionDTO> versions = null;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder size(Long size) {
      this.size = size;
      return this;
    }

    public Builder mimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder versions(List<DocumentFileVersionDTO> versions) {
      this.versions = versions;
      return this;
    }
    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentFileDTO build();
  }
}