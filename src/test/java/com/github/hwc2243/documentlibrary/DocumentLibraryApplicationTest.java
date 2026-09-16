package com.github.hwc2243.documentlibrary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.hwc2243.documentlibrary.dto.DocumentFolderDTO;
import com.github.hwc2243.documentlibrary.dto.DocumentLibraryDTO;
import com.github.hwc2243.documentlibrary.service.DocumentFolderService;
import com.github.hwc2243.documentlibrary.service.DocumentLibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocumentLibraryApplicationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private DocumentLibraryService documentLibraryService;

  @Autowired
  private DocumentFolderService documentFolderService;

  @Test
  void standaloneApplicationRendersDocumentLibraryShell() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      "http://localhost:" + port + "/dl",
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Document Library"));
  }

  @Test
  void standaloneApplicationAcceptsTrailingSlashAtBaseUrl() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      "http://localhost:" + port + "/dl/",
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Document Library"));
  }

  @Test
  void selectedFolderShowsFullHierarchyBreadcrumbAndFolderActions() throws Exception {
    DocumentLibraryDTO library = new DocumentLibraryDTO();
    library.setName("Test Library");
    library = documentLibraryService.create(library);
    DocumentFolderDTO folder = new DocumentFolderDTO();
    folder.setName("Test Folder");
    folder.setLibrary(library);
    folder = documentFolderService.create(folder);
    DocumentFolderDTO childFolder = new DocumentFolderDTO();
    childFolder.setName("Test Subfolder");
    childFolder.setLibrary(library);
    childFolder.setParentFolder(folder);
    childFolder = documentFolderService.create(childFolder);

    ResponseEntity<String> response = restTemplate.getForEntity(
      "http://localhost:" + port + "/dl/folders/" + childFolder.getId(),
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Document Library"));
    assertTrue(response.getBody().contains("Test Library"));
    assertTrue(response.getBody().contains("Test Folder"));
    assertTrue(response.getBody().contains("Test Subfolder"));
    assertTrue(response.getBody().contains("Upload file"));
    assertTrue(response.getBody().contains("+ Folder"));
    assertTrue(!response.getBody().contains("Library root"));
  }

  @Test
  void standaloneApplicationAcceptsLibraryCreationForm() {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("name", "Controller test library");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    restTemplate.postForEntity(
      "http://localhost:" + port + "/dl/libraries",
      new HttpEntity<>(form, headers),
      String.class
    );
    ResponseEntity<String> response = restTemplate.getForEntity(
      "http://localhost:" + port + "/dl",
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().contains("Controller test library"));
  }
}
