package com.github.hwc2243.documentlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFolderEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentFolderPersistence;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentFolderServiceImplTest {

  @TempDir
  Path temporaryDirectory;

  @Test
  void getLibraryPathBuildsHierarchyFromParentFolders() throws ServiceException {
    DocumentLibraryDTO documentLibrary = documentLibrary(10L);
    DocumentFolderDTO rootFolder = folder(20L, documentLibrary, null);
    DocumentFolderDTO childFolder = folder(30L, documentLibrary, rootFolder);
    DocumentFolderDTO grandchildFolder = folder(40L, documentLibrary, childFolder);
    DocumentFolderServiceImpl service = new DocumentFolderServiceImpl();
    service.documentLibraryService = documentLibraryService();

    Path folderPath = service.getLibraryPath(grandchildFolder);

    assertEquals(temporaryDirectory.resolve("10/20/30/40"), folderPath);
  }

  @Test
  void getLibraryPathRequiresPersistedFolder() {
    DocumentFolderServiceImpl service = new DocumentFolderServiceImpl();
    service.documentLibraryService = documentLibraryService();

    ServiceException exception = assertThrows(
      ServiceException.class,
      () -> service.getLibraryPath(new DocumentFolderDTO())
    );

    assertTrue(exception.getMessage().contains("with an ID"));
  }

  @Test
  @SuppressWarnings("unchecked")
  void createPersistsFolderThenCreatesItsHierarchicalDirectory() throws ServiceException {
    DocumentLibraryDTO documentLibrary = documentLibrary(10L);
    DocumentFolderDTO parentFolder = folder(20L, documentLibrary, null);
    DocumentFolderDTO requestedFolder = folder(null, documentLibrary, parentFolder);
    DocumentFolderDTO persistedFolder = new DocumentFolderDTO();
    persistedFolder.setId(30L);
    DocumentFolderEntity documentFolderEntity = new DocumentFolderEntity();
    DocumentFolderEntity[] savedEntity = new DocumentFolderEntity[1];
    BaseDocumentFolderPersistence<DocumentFolderEntity, Long> persistence =
      (BaseDocumentFolderPersistence<DocumentFolderEntity, Long>) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class<?>[] { BaseDocumentFolderPersistence.class },
        (proxy, method, arguments) -> {
          if (method.getName().equals("save")) {
            savedEntity[0] = (DocumentFolderEntity) arguments[0];
            return documentFolderEntity;
          }

          throw new UnsupportedOperationException(method.getName());
        }
      );
    TestDocumentFolderService service = new TestDocumentFolderService(
      documentFolderEntity,
      persistedFolder
    );
    service.documentLibraryService = documentLibraryService();
    ReflectionTestUtils.setField(service, "baseDocumentFolderPersistence", persistence);

    DocumentFolderDTO createdFolder = service.create(requestedFolder);

    assertEquals(persistedFolder, createdFolder);
    assertEquals(30L, requestedFolder.getId());
    assertEquals(documentFolderEntity, savedEntity[0]);
    assertTrue(Files.isDirectory(temporaryDirectory.resolve("10/20/30")));
  }

  private DocumentLibraryService documentLibraryService() {
    return (DocumentLibraryService) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentLibraryService.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("getLibraryPath")) {
          DocumentLibraryDTO documentLibrary = (DocumentLibraryDTO) arguments[0];
          return temporaryDirectory.resolve(documentLibrary.getId().toString());
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
  }

  private static DocumentLibraryDTO documentLibrary(Long id) {
    DocumentLibraryDTO documentLibrary = new DocumentLibraryDTO();
    documentLibrary.setId(id);
    return documentLibrary;
  }

  private static DocumentFolderDTO folder(
    Long id,
    DocumentLibraryDTO documentLibrary,
    DocumentFolderDTO parentFolder
  ) {
    DocumentFolderDTO documentFolder = new DocumentFolderDTO();
    documentFolder.setId(id);
    documentFolder.setLibrary(documentLibrary);
    documentFolder.setParentFolder(parentFolder);
    return documentFolder;
  }

  private static final class TestDocumentFolderService extends DocumentFolderServiceImpl {

    private final DocumentFolderEntity entity;
    private final DocumentFolderDTO persistedDocumentFolder;

    private TestDocumentFolderService(
      DocumentFolderEntity entity,
      DocumentFolderDTO persistedDocumentFolder
    ) {
      this.entity = entity;
      this.persistedDocumentFolder = persistedDocumentFolder;
    }

    @Override
    protected DocumentFolderEntity toEntity(DocumentFolderDTO dto) {
      return entity;
    }

    @Override
    protected DocumentFolderDTO toDto(DocumentFolderEntity entity) {
      return persistedDocumentFolder;
    }
  }
}
