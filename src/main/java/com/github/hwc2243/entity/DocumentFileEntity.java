package com.github.hwc2243.entity;


import com.github.hwc2243.entity.base.BaseDocumentFileEntity;
import com.github.hwc2243.entity.DocumentFileVersionEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import com.github.hwc2243.model.DocumentFile;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity(name="DocumentFile")
@Table(name="documentFile")
public class DocumentFileEntity
    extends BaseDocumentFileEntity<DocumentFileEntity>
    implements DocumentFile<DocumentLibraryEntity, DocumentFolderEntity, DocumentFileVersionEntity>, Serializable
{
	public DocumentFileEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private DocumentFileEntity (Builder builder)
    {
        this.size = builder.size;
        this.mimeType = builder.mimeType;
    }

    public static class Builder extends DocumentObjectEntity.Builder<Builder> {

        private Long size = null;
        private String mimeType = null;

        public Builder size(Long size) {
            this.size = size;
            return this;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        /**
         * The build method creates and returns the immutable Entity object.
         */
        public DocumentFileEntity build() {
            return new DocumentFileEntity(this);
        }
    }
}