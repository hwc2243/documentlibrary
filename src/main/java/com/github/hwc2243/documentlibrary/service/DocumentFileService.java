package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFileService;
import java.io.InputStream;
import java.nio.file.Path;

public interface DocumentFileService extends BaseDocumentFileService<DocumentFileDTO,Long>
{
  Path getLibraryPath(DocumentFileDTO documentFile) throws ServiceException;

  DocumentFileVersionDTO store(DocumentFileDTO documentFile, byte[] content) throws ServiceException;

  DocumentFileVersionDTO store(DocumentFileDTO documentFile, InputStream content) throws ServiceException;
}
