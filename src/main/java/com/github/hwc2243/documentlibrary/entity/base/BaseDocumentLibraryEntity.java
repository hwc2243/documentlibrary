package com.github.hwc2243.documentlibrary.entity.base;


import com.github.hwc2243.documentlibrary.entity.DocumentObjectEntity;
import com.github.hwc2243.documentlibrary.model.base.BaseDocumentLibrary;
import com.github.hwc2243.documentlibrary.model.DocumentLibrary;
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
public abstract class BaseDocumentLibraryEntity<T extends BaseDocumentLibraryEntity<T>> extends AbstractBaseEntity
    implements BaseDocumentLibrary<DocumentObjectEntity>, Serializable
{
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id = null;

  @Column
  protected String name = null;
  

  @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  protected List<DocumentObjectEntity> objects;
  
  
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
  

  public List<DocumentObjectEntity> getObjects ()
  {
    return this.objects;
  }
  
  public void setObjects (List<DocumentObjectEntity> objects)
  {
    this.objects = objects;
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
			
		BaseDocumentLibraryEntity other = (BaseDocumentLibraryEntity) obj;
		return Objects.equals(getId(), other.getId());
	}

}