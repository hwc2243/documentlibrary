package com.github.hwc2243.dto.base;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.hwc2243.dto.DocumentFileDTO;
import com.github.hwc2243.dto.DocumentFileVersionDTO;
import com.github.hwc2243.model.base.BaseDocumentFileVersion;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class BaseDocumentFileVersionDTO
implements BaseDocumentFileVersion<DocumentFileDTO>,  Serializable
{
  protected Long id = null;

  protected Long version = null;
  
  protected Long size = null;
  
  protected String mimeType = null;
  
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
  protected LocalDateTime createDate = null;
  
  protected DocumentFileDTO documentFile;


  protected BaseDocumentFileVersionDTO () {
  }
  
  // Private constructor to force the use of the Builder
  protected BaseDocumentFileVersionDTO (Builder builder)
  {
    this.id = builder.id;
    this.version = builder.version;
    this.size = builder.size;
    this.mimeType = builder.mimeType;
    this.createDate = builder.createDate;
    this.documentFile = builder.documentFile;
  }

  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }


  public Long getVersion ()
  {
    return this.version;
  }
  
  public void setVersion (Long version)
  {
    this.version = version;
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
  

  public LocalDateTime getCreateDate ()
  {
    return this.createDate;
  }
  
  public void setCreateDate (LocalDateTime createDate)
  {
    this.createDate = createDate;
  }
  

  public DocumentFileDTO getDocumentFile ()
  {
    return (DocumentFileDTO)this.documentFile;
  }
  
  public void setDocumentFile (DocumentFileDTO documentFile)
  {
    this.documentFile = documentFile;
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
			
		BaseDocumentFileVersionDTO other = (BaseDocumentFileVersionDTO) obj;
		return id == other.id;
	}

  public abstract static class Builder {

  private Long id = null;

  private Long version = null;
  
  private Long size = null;
  
  private String mimeType = null;
  
  private LocalDateTime createDate = null;
  

    private DocumentFileDTO documentFile = null;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }
    
    public Builder version(Long version) {
      this.version = version;
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

    public Builder createDate(LocalDateTime createDate) {
      this.createDate = createDate;
      return this;
    }

    public Builder documentFile(DocumentFileDTO documentFile) {
      this.documentFile = documentFile;
      return this;
    }
    /**
     * The build method creates and returns the immutable Entity object.
     */
    public abstract DocumentFileVersionDTO build();
  }
}