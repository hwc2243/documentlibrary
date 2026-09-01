package com.github.hwc2243.persistence.base;


import com.github.hwc2243.entity.base.BaseDocumentFileEntity;
import com.github.hwc2243.entity.DocumentFileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentFilePersistence<T extends BaseDocumentFileEntity, ID> extends JpaRepository<T, ID>
{
} 