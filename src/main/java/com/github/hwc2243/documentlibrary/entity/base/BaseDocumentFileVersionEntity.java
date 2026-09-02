package com.github.hwc2243.documentlibrary.entity.base;


import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.model.base.BaseDocumentFileVersion;
import com.github.hwc2243.documentlibrary.model.DocumentFileVersion;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class BaseDocumentFileVersionEntity<T extends BaseDocumentFileVersionEntity<T>> extends AbstractBaseEntity
    implements BaseDocumentFileVersion<DocumentFileEntity>, Serializable
{
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id = null;

  @Column
  protected Long version = null;
  
  @Column
  protected Long size = null;
  
  @Column
  protected String mimeType = null;
  
  @Column
  protected LocalDateTime createDate = null;
  

  @ManyToOne
  @JoinColumn(name= "documentFileId", nullable=true)
  protected DocumentFileEntity documentFile;

  
  public Long getId ()
  {
    return this.id;
  }
  
  public void setId (Long id)
  {
    this.id = id;
  }

  public Object getKey ()
  {
    return this.id;
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
  

  public DocumentFileEntity getDocumentFile ()
  {
    return this.documentFile;
  }
  
  public void setDocumentFile (DocumentFileEntity documentFile)
  {
    this.documentFile = documentFile;
  }



    @Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
			
		BaseDocumentFileVersionEntity other = (BaseDocumentFileVersionEntity) obj;
		return Objects.equals(getId(), other.getId());
	}

}