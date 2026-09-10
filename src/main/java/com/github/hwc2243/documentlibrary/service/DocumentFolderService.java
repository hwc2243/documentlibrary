package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFolderService;
import java.nio.file.Path;
import java.util.List;

public interface DocumentFolderService extends BaseDocumentFolderService<DocumentFolderDTO,Long>
{
  DocumentFolderDTO fetchByName(
    String name,
    DocumentLibraryDTO documentLibrary,
    DocumentFolderDTO parentFolder
  ) throws ServiceException;

  List<DocumentFolderDTO> findFolders(
    DocumentLibraryDTO documentLibrary,
    DocumentFolderDTO parentFolder
  ) throws ServiceException;

  Path getLibraryPath(DocumentFolderDTO documentFolder) throws ServiceException;
}
