package com.github.hwc2243.documentlibrary.service.base;


import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFileVersionDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseDocumentFileVersionService<D extends BaseDocumentFileVersionDTO, ID> extends EntityService<D, ID> {
}