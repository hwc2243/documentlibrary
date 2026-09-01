package com.github.hwc2243.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.dto.DocumentLibraryDTO;
import com.github.hwc2243.dto.DocumentObjectDTO;
import com.github.hwc2243.model.base.BaseDocumentLibrary;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentLibraryDTO
implements BaseDocumentLibrary<DocumentObjectDTO>,  Serializable
{
  protected Long id = null;

  protected String name = null;
  
  protected List<DocumentObjectDTO> objects;


  protected BaseDocumentLibraryDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentLibraryDTO (Builder builder)
  {
    this.id = builder.id;
    this.name = builder.name;
    this.objects = builder.objects;
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
  

  public List<DocumentObjectDTO> getObjects ()
  {
    return this.objects;
  }
  
  public void setObjects (List<DocumentObjectDTO> objects)
  {
    this.objects = objects;
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
  

    private List<DocumentObjectDTO> objects = null;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder objects(List<DocumentObjectDTO> objects) {
      this.objects = objects;
      return this;
    }
    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentLibraryDTO build();
  }
}