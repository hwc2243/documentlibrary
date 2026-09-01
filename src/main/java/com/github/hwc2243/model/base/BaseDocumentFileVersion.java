package com.github.hwc2243.model.base;

import com.github.hwc2243.model.DocumentFile;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public interface BaseDocumentFileVersion<DOCUMENTFILE>
 extends Serializable
{ 
  public Long getId ();
  public void setId (Long id);


  public Long getVersion ();
  public void setVersion (Long version);
  

  public Long getSize ();
  public void setSize (Long size);
  

  public String getMimeType ();
  public void setMimeType (String mimeType);
  

  public LocalDateTime getCreateDate ();
  public void setCreateDate (LocalDateTime createDate);
  

  public DOCUMENTFILE getDocumentFile ();
  public void setDocumentFile (DOCUMENTFILE documentFile);
  

}
