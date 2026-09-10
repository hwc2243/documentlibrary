package com.github.hwc2243.documentlibrary.service.base;


import com.github.hwc2243.documentlibrary.dto.base.BaseDocumentFileDTO;
import java.util.List;

public interface BaseDocumentFileService<D extends BaseDocumentFileDTO, ID> extends EntityService<D, ID> {

	public D fetchByNameAndLibraryIdAndParentFolderId (String name, Long libraryId, Long parentFolderId);

	public List<D> findByLibraryIdAndParentFolderId (Long libraryId, Long parentFolderId);
}
