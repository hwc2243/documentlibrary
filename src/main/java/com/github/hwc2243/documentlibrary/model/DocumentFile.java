package com.github.hwc2243.documentlibrary.model;

import com.github.hwc2243.documentlibrary.model.base.BaseDocumentFile;
import com.github.hwc2243.documentlibrary.model.DocumentFileVersion;
import com.github.hwc2243.documentlibrary.model.DocumentFolder;
import com.github.hwc2243.documentlibrary.model.DocumentLibrary;
import com.github.hwc2243.documentlibrary.model.DocumentObject;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface DocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTFILEVERSION> extends DocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>, BaseDocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTFILEVERSION>
{ 
}
