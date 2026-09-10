package com.github.hwc2243.documentlibrary.service;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.service.base.BaseDocumentFileVersionService;
import java.util.List;

public interface DocumentFileVersionService extends BaseDocumentFileVersionService<DocumentFileVersionDTO,Long>
{
  List<DocumentFileVersionDTO> findVersions(DocumentFileDTO documentFile) throws ServiceException;
}
