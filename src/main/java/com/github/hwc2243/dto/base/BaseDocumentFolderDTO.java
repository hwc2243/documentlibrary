package com.github.hwc2243.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.dto.base.BaseDocumentObjectDTO;
import com.github.hwc2243.dto.DocumentFolderDTO;
import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.dto.DocumentObjectDTO;
import com.github.hwc2243.model.base.BaseDocumentFolder;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentFolderDTO
extends BaseDocumentObjectDTO
implements BaseDocumentFolder<DocumentLibraryDTO, DocumentFolderDTO, DocumentObjectDTO>,  Serializable
{
  protected Long id = null;

  protected List<DocumentObjectDTO> children;


  protected BaseDocumentFolderDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentFolderDTO (Builder builder)
  {
    this.id = builder.id;
    this.children = builder.children;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }


  public List<DocumentObjectDTO> getChildren ()
  {
    return this.children;
  }
  
  public void setChildren (List<DocumentObjectDTO> children)
  {
    this.children = children;
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


    private List<DocumentObjectDTO> children = null;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder children(List<DocumentObjectDTO> children) {
      this.children = children;
      return this;
    }
    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentFolderDTO build();
  }
}