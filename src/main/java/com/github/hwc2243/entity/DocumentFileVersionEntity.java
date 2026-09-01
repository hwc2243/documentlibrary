package com.github.hwc2243.entity;


import com.github.hwc2243.entity.base.BaseDocumentFileVersionEntity;
import com.github.hwc2243.entity.DocumentFileEntity;
import com.github.hwc2243.model.DocumentFileVersion;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name="DocumentFileVersion")
@Table(name="documentFileVersion")
public class DocumentFileVersionEntity
    extends BaseDocumentFileVersionEntity<DocumentFileVersionEntity>
    implements DocumentFileVersion<DocumentFileEntity>, Serializable
{
	public DocumentFileVersionEntity ()
	{
		super();
	}
	
    // Private constructor to force the use of the Builder
    private DocumentFileVersionEntity (Builder builder)
    {
        this.version = builder.version;
        this.size = builder.size;
        this.mimeType = builder.mimeType;
        this.createDate = builder.createDate;
    }

    public static class Builder {

        private Long version = null;
        private Long size = null;
        private String mimeType = null;
        private LocalDateTime createDate = null;

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

        /**
         * The build method creates and returns the immutable Entity object.
         */
        public DocumentFileVersionEntity build() {
            return new DocumentFileVersionEntity(this);
        }
    }
}