package com.github.hwc2243.documentlibrary.model;

import com.github.hwc2243.documentlibrary.model.base.BaseDocumentFile;
import com.github.hwc2243.documentlibrary.model.DocumentObject;

public interface DocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER>
  extends DocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>,
  BaseDocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER>
{ 
}
