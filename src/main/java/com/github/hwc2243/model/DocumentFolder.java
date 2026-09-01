package com.github.hwc2243.model;

import com.github.hwc2243.model.base.BaseDocumentFolder;
import com.github.hwc2243.model.DocumentFolder;
import com.github.hwc2243.model.DocumentLibrary;
import com.github.hwc2243.model.DocumentObject;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface DocumentFolder<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTOBJECT> extends DocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>, BaseDocumentFolder<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTOBJECT>
{ 
}
