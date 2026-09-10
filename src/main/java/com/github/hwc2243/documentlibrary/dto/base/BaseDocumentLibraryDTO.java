package com.github.hwc2243.documentlibrary.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.model.base.BaseDocumentLibrary;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentLibraryDTO
implements BaseDocumentLibrary,  Serializable
{
  protected Long id = null;

  protected String name = null;
  

  protected BaseDocumentLibraryDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentLibraryDTO (Builder builder)
  {
    this.id = builder.id;
    this.name = builder.name;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }


  public String getName ()
  {
    return this.name;
  }
  
  public void setName (String name)
  {
    this.name = name;
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
			
		BaseDocumentLibraryDTO other = (BaseDocumentLibraryDTO) obj;
		return id == other.id;
	}

  public abstract static class Builder {

  private Long id = null;

  private String name = null;
  


    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentLibraryDTO build();
  }
}