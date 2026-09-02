package com.github.hwc2243.documentlibrary.persistence.base;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentLibraryPersistence<T extends BaseDocumentLibraryEntity, ID> extends JpaRepository<T, ID>
{
} 