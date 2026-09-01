package com.github.hwc2243.entity.base;


import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.model.base.BaseDocumentObject;
import com.github.hwc2243.model.DocumentObject;
import com.github.hwc2243.model.DocumentObjectObjectType;
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
public abstract class BaseDocumentObjectEntity<T extends BaseDocumentObjectEntity<T>> extends AbstractBaseEntity
    implements BaseDocumentObject<DocumentLibraryEntity, DocumentFolderEntity>, Serializable
{
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id = null;

  @Column
  protected String name = null;
  
  @Column
  @Enumerated(EnumType.STRING)
  protected DocumentObjectObjectType objectType = null;
  

  @ManyToOne
  @JoinColumn(name= "libraryId", nullable=true)
  protected DocumentLibraryEntity library;

  @ManyToOne
  @JoinColumn(name= "parentFolderId", nullable=true)
  protected DocumentFolderEntity parentFolder;

  
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
  

  public String getName ()
  {
    return this.name;
  }
  
  public void setName (String name)
  {
    this.name = name;
  }
  

  public DocumentObjectObjectType getObjectType ()
  {
    return this.objectType;
  }
  
  public void setObjectType (DocumentObjectObjectType objectType)
  {
    this.objectType = objectType;
  }

  public DocumentLibraryEntity getLibrary ()
  {
    return this.library;
  }
  
  public void setLibrary (DocumentLibraryEntity library)
  {
    this.library = library;
  }


  public DocumentFolderEntity getParentFolder ()
  {
    return this.parentFolder;
  }
  
  public void setParentFolder (DocumentFolderEntity parentFolder)
  {
    this.parentFolder = parentFolder;
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
			
		BaseDocumentObjectEntity other = (BaseDocumentObjectEntity) obj;
		return Objects.equals(getId(), other.getId());
	}

}