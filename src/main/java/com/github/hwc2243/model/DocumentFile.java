package com.github.hwc2243.model;

import com.github.hwc2243.model.base.BaseDocumentFile;
import com.github.hwc2243.model.DocumentFileVersion;
import com.github.hwc2243.model.DocumentFolder;
import com.github.hwc2243.model.DocumentLibrary;
import com.github.hwc2243.model.DocumentObject;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface DocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTFILEVERSION> extends DocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>, BaseDocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTFILEVERSION>
{ 
}
