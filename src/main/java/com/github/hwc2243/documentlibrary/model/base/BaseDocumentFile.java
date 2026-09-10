package com.github.hwc2243.documentlibrary.model.base;

import com.github.hwc2243.documentlibrary.model.DocumentFolder;
import com.github.hwc2243.documentlibrary.model.DocumentLibrary;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentFile<DOCUMENTLIBRARY, DOCUMENTFOLDER>
 extends BaseDocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>, Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public Long getSize ();
  public void setSize (Long size);
  

  public String getMimeType ();
  public void setMimeType (String mimeType);
  

}
