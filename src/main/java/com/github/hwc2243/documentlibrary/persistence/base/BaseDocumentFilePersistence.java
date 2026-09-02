package com.github.hwc2243.documentlibrary.persistence.base;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFileEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentFilePersistence<T extends BaseDocumentFileEntity, ID> extends JpaRepository<T, ID>
{
} 