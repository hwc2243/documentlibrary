package com.github.hwc2243.documentlibrary.controller;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.service.DocumentFileService;
import com.github.hwc2243.documentlibrary.service.DocumentFileVersionService;
import com.github.hwc2243.documentlibrary.service.DocumentFolderService;
import com.github.hwc2243.documentlibrary.service.DocumentLibraryService;
import com.github.hwc2243.documentlibrary.service.ServiceException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** Web browser for navigating and managing document libraries. */
@Controller
@RequestMapping("${documentlibrary.baseurl:/document-library}")
public class DocumentLibraryController {

  @Value("${documentlibrary.baseurl:/document-library}")
  private String baseUrl;

  @Autowired
  private DocumentLibraryService documentLibraryService;

  @Autowired
  private DocumentFolderService documentFolderService;

  @Autowired
  private DocumentFileService documentFileService;

  @Autowired
  private DocumentFileVersionService documentFileVersionService;

  @GetMapping({ "", "/" })
  public String index(Model model) throws ServiceException {
    model.addAttribute("libraries", documentLibraryService.findAll());
    model.addAttribute("baseUrl", normalizedBaseUrl());
    model.addAttribute("pageTitle", "Libraries");
    return "documentlibrary/index";
  }

  @GetMapping("/libraries/{libraryId}")
  public String library(@PathVariable("libraryId") Long libraryId, Model model) throws ServiceException {
    DocumentLibraryDTO library = requiredLibrary(libraryId);

    model.addAttribute("library", library);
    model.addAttribute("folders", documentFolderService.findFolders(library, null));
    model.addAttribute("fileRows", List.of());
    model.addAttribute("baseUrl", normalizedBaseUrl());
    model.addAttribute("pageTitle", library.getName());
    return "documentlibrary/index";
  }

  @GetMapping("/folders/{folderId}")
  public String folder(@PathVariable("folderId") Long folderId, Model model) throws ServiceException {
    DocumentFolderDTO folder = requiredFolder(folderId);
    DocumentLibraryDTO library = requiredLibrary(folder.getLibrary().getId());
    folder.setLibrary(library);
    List<DocumentFileDTO> files = documentFileService.findFiles(folder);

    model.addAttribute("library", library);
    model.addAttribute("folder", folder);
    model.addAttribute("folderBreadcrumbs", folderBreadcrumbs(folder));
    model.addAttribute("folders", documentFolderService.findFolders(null, folder));
    List<FileRow> fileRows = new java.util.ArrayList<>();
    for (DocumentFileDTO file : files) {
      fileRows.add(new FileRow(file, documentFileVersionService.findVersions(file)));
    }
    model.addAttribute("fileRows", fileRows);
    model.addAttribute("baseUrl", normalizedBaseUrl());
    model.addAttribute("pageTitle", folder.getName());
    return "documentlibrary/index";
  }

  @PostMapping("/libraries")
  public String createLibrary(@RequestParam("name") String name, RedirectAttributes attributes) {
    try {
      DocumentLibraryDTO library = new DocumentLibraryDTO();
      library.setName(requiredName(name, "Library"));
      DocumentLibraryDTO created = documentLibraryService.create(library);
      attributes.addFlashAttribute("success", "Library created.");
      return redirect("/libraries/" + created.getId());
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "");
    }
  }

  @PostMapping("/libraries/{libraryId}/folders")
  public String createRootFolder(
    @PathVariable("libraryId") Long libraryId,
    @RequestParam("name") String name,
    RedirectAttributes attributes
  ) {
    try {
      DocumentLibraryDTO library = requiredLibrary(libraryId);
      DocumentFolderDTO folder = new DocumentFolderDTO();
      folder.setName(requiredName(name, "Folder"));
      folder.setLibrary(library);
      documentFolderService.create(folder);
      attributes.addFlashAttribute("success", "Folder created.");
      return redirect("/libraries/" + libraryId);
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "/libraries/" + libraryId);
    }
  }

  @PostMapping("/folders/{folderId}/folders")
  public String createChildFolder(
    @PathVariable("folderId") Long folderId,
    @RequestParam("name") String name,
    RedirectAttributes attributes
  ) {
    try {
      DocumentFolderDTO parentFolder = requiredFolder(folderId);
      DocumentFolderDTO folder = new DocumentFolderDTO();
      folder.setName(requiredName(name, "Folder"));
      folder.setLibrary(parentFolder.getLibrary());
      folder.setParentFolder(parentFolder);
      documentFolderService.create(folder);
      attributes.addFlashAttribute("success", "Folder created.");
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "/folders/" + folderId);
    }

    return redirect("/folders/" + folderId);
  }

  @PostMapping("/folders/{folderId}/files")
  public String uploadFile(
    @PathVariable("folderId") Long folderId,
    @RequestParam("file") MultipartFile upload,
    RedirectAttributes attributes
  ) {
    try {
      DocumentFolderDTO parentFolder = requiredFolder(folderId);

      if (upload.isEmpty() || !StringUtils.hasText(upload.getOriginalFilename())) {
        throw new ServiceException("Select a file to upload.");
      }

      String name = StringUtils.cleanPath(upload.getOriginalFilename());
      DocumentFileDTO documentFile = documentFileService.fetchByName(name, parentFolder);

      if (documentFile == null) {
        documentFile = new DocumentFileDTO();
        documentFile.setName(name);
        documentFile.setLibrary(parentFolder.getLibrary());
        documentFile.setParentFolder(parentFolder);
      }

      documentFile.setMimeType(upload.getContentType());
      documentFileService.store(documentFile, upload.getInputStream());
      attributes.addFlashAttribute("success", "File uploaded; an existing file received a new version.");
    }
    catch (Exception exception) {
      return errorRedirect(attributes, exception, "/folders/" + folderId);
    }

    return redirect("/folders/" + folderId);
  }

  @GetMapping("/files/{fileId}/download")
  public ResponseEntity<InputStreamResource> downloadLatest(@PathVariable("fileId") Long fileId) throws ServiceException {
    DocumentFileDTO documentFile = requiredFile(fileId);
    return download(documentFile, documentFileService.load(documentFile));
  }

  @GetMapping("/files/{fileId}/versions/{versionId}/download")
  public ResponseEntity<InputStreamResource> downloadVersion(
    @PathVariable("fileId") Long fileId,
    @PathVariable("versionId") Long versionId
  ) throws ServiceException {
    DocumentFileDTO documentFile = requiredFile(fileId);
    return download(documentFile, documentFileService.loadVersion(documentFile, versionId));
  }

  @PostMapping("/libraries/{libraryId}/delete")
  public String deleteDocumentLibrary(@PathVariable("libraryId") Long libraryId, RedirectAttributes attributes) {
    try {
      documentLibraryService.delete(requiredLibrary(libraryId));
      attributes.addFlashAttribute("success", "Library deleted.");
      return redirect("");
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "/libraries/" + libraryId);
    }
  }

  @PostMapping("/folders/{folderId}/delete")
  public String deleteFolder(@PathVariable("folderId") Long folderId, RedirectAttributes attributes) {
    try {
      DocumentFolderDTO folder = requiredFolder(folderId);
      Long parentId = folder.getParentFolder() == null ? null : folder.getParentFolder().getId();
      Long libraryId = folder.getLibrary().getId();
      documentFolderService.delete(folder);
      attributes.addFlashAttribute("success", "Folder deleted.");
      return parentId == null ? redirect("/libraries/" + libraryId) : redirect("/folders/" + parentId);
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "/folders/" + folderId);
    }
  }

  @PostMapping("/files/{fileId}/delete")
  public String deleteDocumentFile(@PathVariable("fileId") Long fileId, RedirectAttributes attributes) {
    try {
      DocumentFileDTO file = requiredFile(fileId);
      Long parentId = file.getParentFolder() == null ? null : file.getParentFolder().getId();
      documentFileService.delete(file);
      attributes.addFlashAttribute("success", "File and its versions deleted.");
      return parentId == null ? redirect("") : redirect("/folders/" + parentId);
    }
    catch (ServiceException exception) {
      return errorRedirect(attributes, exception, "");
    }
  }

  private ResponseEntity<InputStreamResource> download(DocumentFileDTO documentFile, InputStream stream) {
    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

    if (StringUtils.hasText(documentFile.getMimeType())) {
      try {
        mediaType = MediaType.parseMediaType(documentFile.getMimeType());
      }
      catch (IllegalArgumentException ignored) {
        // Use the safe binary default when an uploaded content type is invalid.
      }
    }

    return ResponseEntity.ok()
      .contentType(mediaType)
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        ContentDisposition.attachment().filename(documentFile.getName()).build().toString()
      )
      .body(new InputStreamResource(stream));
  }

  private DocumentLibraryDTO requiredLibrary(Long id) throws ServiceException {
    DocumentLibraryDTO library = documentLibraryService.get(id);
    if (library == null) {
      throw new ServiceException("Document library not found: " + id);
    }
    return library;
  }

  private DocumentFolderDTO requiredFolder(Long id) throws ServiceException {
    DocumentFolderDTO folder = documentFolderService.get(id);
    if (folder == null) {
      throw new ServiceException("Document folder not found: " + id);
    }
    return folder;
  }

  private DocumentFileDTO requiredFile(Long id) throws ServiceException {
    DocumentFileDTO file = documentFileService.get(id);
    if (file == null) {
      throw new ServiceException("Document file not found: " + id);
    }
    return file;
  }

  /**
   * Resolves the synthetic parent-folder references returned by the service into
   * complete DTOs so the view can render the full path from the library root.
   */
  private List<DocumentFolderDTO> folderBreadcrumbs(DocumentFolderDTO folder) throws ServiceException {
    Deque<DocumentFolderDTO> breadcrumbs = new ArrayDeque<>();
    Set<Long> visitedFolderIds = new HashSet<>();
    DocumentFolderDTO current = folder;

    while (current != null) {
      if (current.getId() == null || !visitedFolderIds.add(current.getId())) {
        throw new ServiceException("Invalid document folder hierarchy.");
      }

      breadcrumbs.addFirst(current);
      DocumentFolderDTO parent = current.getParentFolder();
      current = parent == null ? null : requiredFolder(parent.getId());
    }

    return new ArrayList<>(breadcrumbs);
  }

  private String requiredName(String value, String type) throws ServiceException {
    if (!StringUtils.hasText(value)) {
      throw new ServiceException(type + " name is required.");
    }
    return value.trim();
  }

  private String errorRedirect(RedirectAttributes attributes, Exception exception, String path) {
    attributes.addFlashAttribute("error", exception.getMessage());
    return redirect(path);
  }

  private String redirect(String path) {
    return "redirect:" + normalizedBaseUrl() + path;
  }

  private String normalizedBaseUrl() {
    return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
  }

  public record FileRow(DocumentFileDTO file, List<DocumentFileVersionDTO> versions) {
  }
}
