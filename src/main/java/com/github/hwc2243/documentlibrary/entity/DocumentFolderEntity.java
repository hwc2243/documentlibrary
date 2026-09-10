package com.github.hwc2243.documentlibrary.entity;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.model.DocumentFolder;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity(name="DocumentFolder")
@Table(name="documentFolder")
public class DocumentFolderEntity
    extends BaseDocumentFolderEntity<DocumentFolderEntity>
    implements DocumentFolder<DocumentLibraryEntity, DocumentFolderEntity>, Serializable
{
	public DocumentFolderEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private DocumentFolderEntity (Builder builder)
    {
    }

    public static class Builder extends DocumentObjectEntity.Builder<Builder> {


        @Override
        protected Builder self() {
            return this;
        }

        /**
         * The build method creates and returns the immutable Entity object.
         */
        public DocumentFolderEntity build() {
            return new DocumentFolderEntity(this);
        }
    }
}
