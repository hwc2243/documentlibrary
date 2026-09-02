package com.github.hwc2243.documentlibrary.service.base;


import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentLibraryDTO;
import java.util.List;

public interface BaseDocumentLibraryService<D extends BaseDocumentLibraryDTO, ID> extends EntityService<D, ID> {
}