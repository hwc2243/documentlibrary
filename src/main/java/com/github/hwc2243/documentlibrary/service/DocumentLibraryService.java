package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentLibraryService;
import java.nio.file.Path;

public interface DocumentLibraryService extends BaseDocumentLibraryService<DocumentLibraryDTO,Long>
{
  Path getLibraryPath(DocumentLibraryDTO documentLibrary) throws ServiceException;
}
