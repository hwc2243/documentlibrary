package com.github.hwc2243.documentlibrary.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileEntity;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.persistence.DocumentFilePersistence;
import com.github.hwc2243.documentlibrary.persistence.DocumentFileVersionPersistence;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentFileServiceImplTest {

  @TempDir
  Path temporaryDirectory;

  @Test
  @SuppressWarnings("unchecked")
  void storeCreatesDistinctVersionFilesForByteArrayAndStreamContent() throws Exception {
    DocumentLibraryDTO documentLibrary = new DocumentLibraryDTO();
    documentLibrary.setId(10L);
    DocumentFolderDTO documentFolder = new DocumentFolderDTO();
    documentFolder.setId(20L);
    documentFolder.setLibrary(documentLibrary);
    DocumentFileDTO documentFile = new DocumentFileDTO();
    documentFile.setId(30L);
    documentFile.setLibrary(documentLibrary);
    documentFile.setParentFolder(documentFolder);
    documentFile.setMimeType("text/plain");
    DocumentFileEntity documentFileEntity = new DocumentFileEntity();
    DocumentFileVersionEntity[] latestVersion = new DocumentFileVersionEntity[1];
    long[] nextVersionId = { 40L };
    DocumentFileServiceImpl service = new DocumentFileServiceImpl();
    service.documentLibraryService = libraryService();
    service.documentFolderService = folderService();
    DocumentFilePersistence documentFilePersistence = (DocumentFilePersistence) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentFilePersistence.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("getReferenceById")) {
          return documentFileEntity;
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
    ReflectionTestUtils.setField(service, "documentFilePersistence", documentFilePersistence);
    service.documentFileVersionPersistence = (DocumentFileVersionPersistence) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentFileVersionPersistence.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("findTopByDocumentFile_IdOrderByVersionDesc")) {
          return Optional.ofNullable(latestVersion[0]);
        }

        if (method.getName().equals("save")) {
          DocumentFileVersionEntity version = (DocumentFileVersionEntity) arguments[0];

          if (version.getId() == null) {
            version.setId(nextVersionId[0]++);
          }

          latestVersion[0] = version;
          return version;
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
    byte[] firstContent = "first version".getBytes(StandardCharsets.UTF_8);
    byte[] secondContent = "second version".getBytes(StandardCharsets.UTF_8);

    DocumentFileVersionDTO firstVersion = service.store(documentFile, firstContent);
    DocumentFileVersionDTO secondVersion = service.store(
      documentFile,
      new ByteArrayInputStream(secondContent)
    );

    Path fileDirectory = temporaryDirectory.resolve("10/20/30");
    assertEquals(40L, firstVersion.getId());
    assertEquals(1L, firstVersion.getVersion());
    assertEquals(41L, secondVersion.getId());
    assertEquals(2L, secondVersion.getVersion());
    assertArrayEquals(firstContent, Files.readAllBytes(fileDirectory.resolve("40")));
    assertArrayEquals(secondContent, Files.readAllBytes(fileDirectory.resolve("41")));
    assertTrue(Files.isDirectory(fileDirectory));
  }

  @Test
  void loadReturnsInputStreamForLatestStoredVersion() throws Exception {
    DocumentLibraryDTO documentLibrary = new DocumentLibraryDTO();
    documentLibrary.setId(10L);
    DocumentFolderDTO documentFolder = new DocumentFolderDTO();
    documentFolder.setId(20L);
    documentFolder.setLibrary(documentLibrary);
    DocumentFileDTO documentFile = new DocumentFileDTO();
    documentFile.setId(30L);
    documentFile.setLibrary(documentLibrary);
    documentFile.setParentFolder(documentFolder);
    DocumentFileVersionEntity latestVersion = new DocumentFileVersionEntity();
    latestVersion.setId(40L);
    latestVersion.setVersion(2L);
    byte[] expectedContent = "latest version".getBytes(StandardCharsets.UTF_8);
    Path versionPath = temporaryDirectory.resolve("10/20/30/40");
    Files.createDirectories(versionPath.getParent());
    Files.write(versionPath, expectedContent);
    DocumentFileServiceImpl service = new DocumentFileServiceImpl();
    service.documentLibraryService = libraryService();
    service.documentFolderService = folderService();
    service.documentFileVersionPersistence = latestVersionPersistence(latestVersion);

    try (InputStream content = service.load(documentFile)) {
      assertArrayEquals(expectedContent, content.readAllBytes());
    }
  }

  @Test
  void loadThrowsWhenNoStoredVersionExists() {
    DocumentLibraryDTO documentLibrary = new DocumentLibraryDTO();
    documentLibrary.setId(10L);
    DocumentFileDTO documentFile = new DocumentFileDTO();
    documentFile.setId(30L);
    documentFile.setLibrary(documentLibrary);
    DocumentFileServiceImpl service = new DocumentFileServiceImpl();
    service.documentLibraryService = libraryService();
    service.documentFolderService = folderService();
    service.documentFileVersionPersistence = latestVersionPersistence(null);

    ServiceException exception = assertThrows(ServiceException.class, () -> service.load(documentFile));

    assertTrue(exception.getMessage().contains("No stored versions exist"));
  }

  private DocumentFileVersionPersistence latestVersionPersistence(DocumentFileVersionEntity latestVersion) {
    return (DocumentFileVersionPersistence) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentFileVersionPersistence.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("findTopByDocumentFile_IdOrderByVersionDesc")) {
          return Optional.ofNullable(latestVersion);
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
  }

  private DocumentLibraryService libraryService() {
    return (DocumentLibraryService) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentLibraryService.class },
      (proxy, method, arguments) -> temporaryDirectory.resolve("10")
    );
  }

  private DocumentFolderService folderService() {
    return (DocumentFolderService) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentFolderService.class },
      (proxy, method, arguments) -> temporaryDirectory.resolve("10/20")
    );
  }
}
