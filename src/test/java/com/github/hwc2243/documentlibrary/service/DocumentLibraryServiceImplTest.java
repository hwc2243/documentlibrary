package com.github.hwc2243.documentlibrary.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.persistence.base.BaseDocumentLibraryPersistence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentLibraryServiceImplTest {

  @TempDir
  Path temporaryDirectory;

  @Test
  void rejectsBlankDocumentLibraryPath() {
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = "   ";

    IllegalStateException exception = assertThrows(
      IllegalStateException.class,
      service::validateDocumentLibraryPath
    );

    assertTrue(exception.getMessage().contains("documentlibrary.path property"));
  }

  @Test
  void acceptsExistingDirectory() {
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = temporaryDirectory.toString();

    assertDoesNotThrow(service::validateDocumentLibraryPath);
  }

  @Test
  void createsMissingDocumentLibraryDirectory() {
    Path documentLibraryDirectory = temporaryDirectory.resolve("documents/library");
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = documentLibraryDirectory.toString();

    assertDoesNotThrow(service::validateDocumentLibraryPath);
    assertTrue(Files.isDirectory(documentLibraryDirectory));
  }

  @Test
  void rejectsPathThatIsAnExistingFile() throws IOException {
    Path documentLibraryFile = Files.createFile(temporaryDirectory.resolve("documents"));
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = documentLibraryFile.toString();

    IllegalStateException exception = assertThrows(
      IllegalStateException.class,
      service::validateDocumentLibraryPath
    );

    assertTrue(exception.getMessage().contains("is not a directory"));
  }

  @Test
  void reportsFailureWhenMissingDirectoryCannotBeCreated() throws IOException {
    Path fileParent = Files.createFile(temporaryDirectory.resolve("file-parent"));
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = fileParent.resolve("documents").toString();

    IllegalStateException exception = assertThrows(
      IllegalStateException.class,
      service::validateDocumentLibraryPath
    );

    assertTrue(exception.getMessage().contains("Unable to create the document library directory"));
  }

  @Test
  void getLibraryPathRequiresPersistedDocumentLibrary() {
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = temporaryDirectory.toString();

    ServiceException exception = assertThrows(
      ServiceException.class,
      () -> service.getLibraryPath(new DocumentLibraryDTO())
    );

    assertTrue(exception.getMessage().contains("with an ID"));
  }

  @Test
  void getLibraryPathAppendsPersistedDocumentLibraryId() throws ServiceException {
    DocumentLibraryDTO documentLibrary = new DocumentLibraryDTO();
    documentLibrary.setId(42L);
    DocumentLibraryServiceImpl service = new DocumentLibraryServiceImpl();
    service.dlPath = temporaryDirectory.toString();

    Path libraryPath = service.getLibraryPath(documentLibrary);

    assertEquals(temporaryDirectory.resolve("42"), libraryPath);
  }

  @Test
  @SuppressWarnings("unchecked")
  void createPersistsDocumentLibraryThenCreatesItsDirectory() throws ServiceException {
    DocumentLibraryDTO requestedDocumentLibrary = new DocumentLibraryDTO();
    DocumentLibraryDTO persistedDocumentLibrary = new DocumentLibraryDTO();
    persistedDocumentLibrary.setId(42L);
    DocumentLibraryEntity documentLibraryEntity = new DocumentLibraryEntity();
    DocumentLibraryEntity[] savedEntity = new DocumentLibraryEntity[1];
    BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long> persistence =
      (BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long>) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class<?>[] { BaseDocumentLibraryPersistence.class },
        (proxy, method, arguments) -> {
          if (method.getName().equals("save")) {
            savedEntity[0] = (DocumentLibraryEntity) arguments[0];
            return documentLibraryEntity;
          }

          throw new UnsupportedOperationException(method.getName());
        }
      );
    TestDocumentLibraryService service = new TestDocumentLibraryService(
      documentLibraryEntity,
      persistedDocumentLibrary
    );
    service.dlPath = temporaryDirectory.toString();
    ReflectionTestUtils.setField(service, "baseDocumentLibraryPersistence", persistence);

    DocumentLibraryDTO createdDocumentLibrary = service.create(requestedDocumentLibrary);

    assertEquals(persistedDocumentLibrary, createdDocumentLibrary);
    assertTrue(Files.isDirectory(temporaryDirectory.resolve("42")));
    assertEquals(documentLibraryEntity, savedEntity[0]);
  }

  @Test
  @SuppressWarnings("unchecked")
  void fetchByNameReturnsMatchingDocumentLibrary() {
    DocumentLibraryEntity documentLibraryEntity = new DocumentLibraryEntity();
    DocumentLibraryDTO expectedDocumentLibrary = new DocumentLibraryDTO();
    expectedDocumentLibrary.setId(42L);
    String[] requestedName = new String[1];
    BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long> persistence =
      (BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long>) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class<?>[] { BaseDocumentLibraryPersistence.class },
        (proxy, method, arguments) -> {
          if (method.getName().equals("findFirstByName")) {
            requestedName[0] = (String) arguments[0];
            return documentLibraryEntity;
          }

          throw new UnsupportedOperationException(method.getName());
        }
      );
    TestDocumentLibraryService service = new TestDocumentLibraryService(
      documentLibraryEntity,
      expectedDocumentLibrary
    );
    ReflectionTestUtils.setField(service, "baseDocumentLibraryPersistence", persistence);

    DocumentLibraryDTO documentLibrary = service.fetchByName("records");

    assertEquals(expectedDocumentLibrary, documentLibrary);
    assertEquals("records", requestedName[0]);
  }

  @Test
  @SuppressWarnings("unchecked")
  void fetchByNameReturnsNullWhenNoDocumentLibraryMatches() {
    BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long> persistence =
      (BaseDocumentLibraryPersistence<DocumentLibraryEntity, Long>) Proxy.newProxyInstance(
        getClass().getClassLoader(),
        new Class<?>[] { BaseDocumentLibraryPersistence.class },
        (proxy, method, arguments) -> {
          if (method.getName().equals("findFirstByName")) {
            return null;
          }

          throw new UnsupportedOperationException(method.getName());
        }
      );
    TestDocumentLibraryService service = new TestDocumentLibraryService(
      new DocumentLibraryEntity(),
      new DocumentLibraryDTO()
    );
    ReflectionTestUtils.setField(service, "baseDocumentLibraryPersistence", persistence);

    DocumentLibraryDTO documentLibrary = service.fetchByName("missing");

    assertEquals(null, documentLibrary);
  }

  private static final class TestDocumentLibraryService extends DocumentLibraryServiceImpl {

    private final DocumentLibraryEntity entity;
    private final DocumentLibraryDTO persistedDocumentLibrary;

    private TestDocumentLibraryService(
      DocumentLibraryEntity entity,
      DocumentLibraryDTO persistedDocumentLibrary
    ) {
      this.entity = entity;
      this.persistedDocumentLibrary = persistedDocumentLibrary;
    }

    @Override
    protected DocumentLibraryEntity toEntity(DocumentLibraryDTO dto) {
      return entity;
    }

    @Override
    protected DocumentLibraryDTO toDto(DocumentLibraryEntity entity) {
      return entity == null ? null : persistedDocumentLibrary;
    }
  }
}
