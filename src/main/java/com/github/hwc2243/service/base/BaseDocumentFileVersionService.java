package com.github.hwc2243.service.base;


import com.github.hwc2243.dto.base.BaseDocumentFileVersionDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface BaseDocumentFileVersionService<D extends BaseDocumentFileVersionDTO, ID> extends EntityService<D, ID> {
}