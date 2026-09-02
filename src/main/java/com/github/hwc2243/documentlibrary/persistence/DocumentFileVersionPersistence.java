package com.github.hwc2243.documentlibrary.persistence;

import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentFileVersionPersistence;
import java.util.Optional;

public interface DocumentFileVersionPersistence extends BaseDocumentFileVersionPersistence<DocumentFileVersionEntity,Long>
{
  Optional<DocumentFileVersionEntity> findTopByDocumentFile_IdOrderByVersionDesc(Long documentFileId);
}
