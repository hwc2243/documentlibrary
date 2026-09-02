package com.github.hwc2243.documentlibrary.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
}
