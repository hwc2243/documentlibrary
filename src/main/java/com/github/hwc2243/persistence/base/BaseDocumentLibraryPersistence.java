package com.github.hwc2243.persistence.base;


import com.github.hwc2243.entity.base.BaseDocumentLibraryEntity;
import com.github.hwc2243.entity.DocumentLibraryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentLibraryPersistence<T extends BaseDocumentLibraryEntity, ID> extends JpaRepository<T, ID>
{
} 