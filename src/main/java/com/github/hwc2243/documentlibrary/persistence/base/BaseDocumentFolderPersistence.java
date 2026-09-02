package com.github.hwc2243.documentlibrary.persistence.base;


import com.github.hwc2243.documentlibrary.entity.base.BaseDocumentFolderEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentFolderPersistence<T extends BaseDocumentFolderEntity, ID> extends JpaRepository<T, ID>
{
} 