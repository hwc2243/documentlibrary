package com.github.hwc2243.persistence.base;


import com.github.hwc2243.entity.base.BaseDocumentFolderEntity;
import com.github.hwc2243.entity.DocumentFolderEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseDocumentFolderPersistence<T extends BaseDocumentFolderEntity, ID> extends JpaRepository<T, ID>
{
} 