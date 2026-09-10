package com.github.hwc2243.documentlibrary.model.base;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentLibrary
 extends Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public String getName ();
  public void setName (String name);
  

}
