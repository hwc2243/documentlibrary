package com.github.hwc2243.documentlibrary.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.hwc2243.documentlibrary.service.DocumentLibraryService;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.test.util.ReflectionTestUtils;

class DocumentLibraryControllerTest {

  @Test
  void indexRendersTheDocumentLibraryShell() throws Exception {
    DocumentLibraryController controller = new DocumentLibraryController();
    DocumentLibraryService libraryService = (DocumentLibraryService) Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class<?>[] { DocumentLibraryService.class },
      (proxy, method, arguments) -> {
        if (method.getName().equals("findAll")) {
          return List.of();
        }

        throw new UnsupportedOperationException(method.getName());
      }
    );
    ReflectionTestUtils.setField(controller, "documentLibraryService", libraryService);
    ReflectionTestUtils.setField(controller, "baseUrl", "/dl");

    assertEquals("documentlibrary/index", controller.index(new ConcurrentModel()));
  }

  @Test
  void basePathIsConfiguredByProperty() {
    RequestMapping mapping = DocumentLibraryController.class.getAnnotation(RequestMapping.class);

    assertEquals(
      "${documentlibrary.baseurl:/document-library}",
      Objects.requireNonNull(mapping).value()[0]
    );
  }
}
