package com.github.hwc2243.entity;


import com.github.hwc2243.entity.base.BaseDocumentObjectEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.model.DocumentObject;
import com.github.hwc2243.model.DocumentObjectObjectType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity(name="DocumentObject")
@Table(name="documentObject")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class DocumentObjectEntity<T extends BaseDocumentObjectEntity<T>>
    extends BaseDocumentObjectEntity<T>
    implements DocumentObject<DocumentLibraryEntity, DocumentFolderEntity>, Serializable
{
	public DocumentObjectEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private DocumentObjectEntity (Builder builder)
    {
        this.name = builder.name;
        this.objectType = builder.objectType;
    }

    public abstract static class Builder<B extends Builder<B>> {

        protected String name = null;
        protected DocumentObjectObjectType objectType = null;

        public B name(String name) {
            this.name = name;
            return self();
        }

        public B objectType(DocumentObjectObjectType objectType) {
            this.objectType = objectType;
            return self();
        }

        protected abstract B self();
    }
}