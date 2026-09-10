package com.github.hwc2243.documentlibrary.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentObjectDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.model.base.BaseDocumentFolder;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentFolderDTO
extends BaseDocumentObjectDTO
implements BaseDocumentFolder<DocumentLibraryDTO, DocumentFolderDTO>,  Serializable
{
  protected Long id = null;


  protected BaseDocumentFolderDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentFolderDTO (Builder builder)
  {
    this.id = builder.id;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
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
			
		BaseDocumentFolderDTO other = (BaseDocumentFolderDTO) obj;
		return id == other.id;
	}

  public abstract static class Builder {

  private Long id = null;



    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentFolderDTO build();
  }
}