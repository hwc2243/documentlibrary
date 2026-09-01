package com.github.hwc2243.entity.base;


import com.github.hwc2243.entity.base.BaseDocumentObjectEntity;
import com.github.hwc2243.entity.DocumentFileVersionEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.model.base.BaseDocumentFile;
import com.github.hwc2243.model.DocumentFile;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class BaseDocumentFileEntity<T extends BaseDocumentFileEntity<T>>
  extends BaseDocumentObjectEntity<T>
    implements BaseDocumentFile<DocumentLibraryEntity, DocumentFolderEntity, DocumentFileVersionEntity>, Serializable
{
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id = null;

  @Column
  protected Long size = null;
  
  @Column
  protected String mimeType = null;
  

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "documentFileId")
  protected List<DocumentFileVersionEntity> versions;
  
  
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
  

  public List<DocumentFileVersionEntity> getVersions ()
  {
    return this.versions;
  }
  
  public void setVersions (List<DocumentFileVersionEntity> versions)
  {
    this.versions = versions;
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
			
		BaseDocumentFileEntity other = (BaseDocumentFileEntity) obj;
		return Objects.equals(getId(), other.getId());
	}

}