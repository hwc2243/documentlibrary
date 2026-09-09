package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderService;
import java.nio.file.Path;

public interface DocumentFolderService extends BaseDocumentFolderService<DocumentFolderDTO,Long>
{
  DocumentFolderDTO fetchByName(String name) throws ServiceException;

  Path getLibraryPath(DocumentFolderDTO documentFolder) throws ServiceException;
}
