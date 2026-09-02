package com.github.hwc2243.documentlibrary.model.base;

import com.github.hwc2243.documentlibrary.model.DocumentFolder;
import com.github.hwc2243.documentlibrary.model.DocumentLibrary;
import com.github.hwc2243.documentlibrary.model.DocumentObject;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentFolder<DOCUMENTLIBRARY, DOCUMENTFOLDER, DOCUMENTOBJECT>
 extends BaseDocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>, Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public List<DOCUMENTOBJECT> getChildren ();
  public void setChildren (List<DOCUMENTOBJECT> children);


}
