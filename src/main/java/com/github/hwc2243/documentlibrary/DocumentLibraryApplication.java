package com.github.hwc2243.documentlibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Standalone launcher for exercising the document-library web component.
 *
 * <p>Run locally with {@code mvn spring-boot:run}; the bundled configuration
 * uses an embedded H2 database.
 */
@SpringBootApplication
public class DocumentLibraryApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocumentLibraryApplication.class, args);
  }
}
