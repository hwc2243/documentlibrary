package com.github.hwc2243.documentlibrary.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.hwc2243.documentlibrary.dto.DocumentFileDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentFileVersionDTO;
import com.github.hwc2243.documentlibrary.entity.DocumentFileVersionEntity;
import com.github.hwc2243.documentlibrary.persistence.DocumentFileVersionPersistence;
import java.lang.reflect.Proxy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentFileVersionServiceImplTest {

  @Test
  void findVersionsUsesTheDocumentFileFinderAndPreservesTheFileReference()
    throws ServiceException {
    DocumentFileDTO documentFile = new DocumentFileDTO();
    documentFile.setId(30L);
    DocumentFileVersionEntity entity = new DocumentFileVersionEntity();
    DocumentFileVersionDTO expectedVersion = new DocumentFileVersionDTO();
    Long[] finderArgument = new Long[1];
    TestDocumentFileVersionService service = new TestDocumentFileVersionService(expectedVersion);
    ReflectionTestUtils.setField(
      service,
      "documentFileVersionPersistence",
      versionPersistence(List.of(entity), finderArgument)
    );

    List<DocumentFileVersionDTO> versions = service.findVersions(documentFile);

    assertEquals(List.of(expectedVersion), versions);
    assertEquals(30L, finderArgument[0]);
    assertEquals(documentFile, versions.get(0).getDocumentFile());
  }

  @Test
  void findVersionsRequiresPersistedDocumentFile() {
    DocumentFileVersionServiceImpl service = new DocumentFileVersionServiceImpl();

    assertThrows(ServiceException.class, () -> service.findVersions(new DocumentFileDTO()));
  }

  private DocumentFileVersionPersistence versionPersistence(
    List<DocumentFileVersionEntity> entities,
    Long[] finderArgument
  ) {
    return (DocumentFileVersionPersistence) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentFileVersionPersistence.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("findByDocumentFileId")) {
          finderArgument[0] = (Long) arguments[0];
          return entities;
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
  }

  private static final class TestDocumentFileVersionService
    extends DocumentFileVersionServiceImpl {

    private final DocumentFileVersionDTO documentFileVersion;

    private TestDocumentFileVersionService(DocumentFileVersionDTO documentFileVersion) {
      this.documentFileVersion = documentFileVersion;
    }

    @Override
    protected List<DocumentFileVersionDTO> toDtos(List<DocumentFileVersionEntity> entities) {
      return entities.stream().map(entity -> documentFileVersion).toList();
    }
  }
}
