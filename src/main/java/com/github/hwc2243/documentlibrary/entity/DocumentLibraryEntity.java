package com.github.hwc2243.documentlibrary.entity;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentObjectEntity;
import com.github.hwc2243.documentlibrary.model.DocumentLibrary;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity(name="DocumentLibrary")
@Table(name="documentLibrary")
public class DocumentLibraryEntity
    extends BaseDocumentLibraryEntity<DocumentLibraryEntity>
    implements DocumentLibrary<DocumentObjectEntity>, Serializable
{
	public DocumentLibraryEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private DocumentLibraryEntity (Builder builder)
    {
        this.name = builder.name;
    }

    public static class Builder {

        private String name = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * The build method creates and returns the immutable Entity object.
         */
        public DocumentLibraryEntity build() {
            return new DocumentLibraryEntity(this);
        }
    }
}