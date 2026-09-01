package com.github.hwc2243.entity.base;


import com.github.hwc2243.entity.base.BaseDocumentObjectEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.entity.DocumentObjectEntity;
import com.github.hwc2243.model.base.BaseDocumentFolder;
import com.github.hwc2243.model.DocumentFolder;
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
public abstract class BaseDocumentFolderEntity<T extends BaseDocumentFolderEntity<T>>
  extends BaseDocumentObjectEntity<T>
    implements BaseDocumentFolder<DocumentLibraryEntity, DocumentFolderEntity, DocumentObjectEntity>, Serializable
{
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id = null;


  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "documentFolderId")
  protected List<DocumentObjectEntity> children;
  
  
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
  

  public List<DocumentObjectEntity> getChildren ()
  {
    return this.children;
  }
  
  public void setChildren (List<DocumentObjectEntity> children)
  {
    this.children = children;
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
			
		BaseDocumentFolderEntity other = (BaseDocumentFolderEntity) obj;
		return Objects.equals(getId(), other.getId());
	}

}