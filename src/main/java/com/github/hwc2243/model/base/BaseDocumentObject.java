package com.github.hwc2243.model.base;

import com.github.hwc2243.model.DocumentFolder;
import com.github.hwc2243.model.DocumentLibrary;
import com.github.hwc2243.model.DocumentObjectObjectType;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentObject<DOCUMENTLIBRARY, DOCUMENTFOLDER>
 extends Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public String getName ();
  public void setName (String name);
  

  public DocumentObjectObjectType getObjectType ();
  public void setObjectType (DocumentObjectObjectType objectType);
  

  public DOCUMENTLIBRARY getLibrary ();
  public void setLibrary (DOCUMENTLIBRARY library);
  

  public DOCUMENTFOLDER getParentFolder ();
  public void setParentFolder (DOCUMENTFOLDER parentFolder);
  

}
