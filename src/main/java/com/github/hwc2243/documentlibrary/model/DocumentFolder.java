package com.github.hwc2243.documentlibrary.model;

import com.github.hwc2243.documentlibrary.model.base.BaseDocumentFolder;
import com.github.hwc2243.documentlibrary.model.DocumentObject;

public interface DocumentFolder<DOCUMENTLIBRARY, DOCUMENTFOLDER>
  extends DocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>,
  BaseDocumentFolder<DOCUMENTLIBRARY, DOCUMENTFOLDER>
{ 
}
