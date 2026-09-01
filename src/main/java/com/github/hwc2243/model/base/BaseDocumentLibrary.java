package com.github.hwc2243.model.base;

import com.github.hwc2243.model.DocumentObject;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentLibrary<DOCUMENTOBJECT>
 extends Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public String getName ();
  public void setName (String name);
  

  public List<DOCUMENTOBJECT> getObjects ();
  public void setObjects (List<DOCUMENTOBJECT> objects);


}
