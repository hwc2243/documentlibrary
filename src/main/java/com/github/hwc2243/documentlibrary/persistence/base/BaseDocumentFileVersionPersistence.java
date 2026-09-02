package com.github.hwc2243.documentlibrary.persistence.base;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentFileVersionPersistence<T extends BaseDocumentFileVersionEntity, ID> extends JpaRepository<T, ID>
{
} 