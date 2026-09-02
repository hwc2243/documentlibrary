package com.github.hwc2243.documentlibrary.config;

import com.github.hwc2243.documentlibrary.entity.DocumentLibraryEntity;
import com.github.hwc2243.documentlibrary.persistence.DocumentLibraryPersistence;
import com.github.hwc2243.documentlibrary.service.DocumentLibraryService;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Registers the document library's Spring services, entities, and repositories.
 *
 * <p>Import this configuration from an application that consumes this library.
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackageClasses = DocumentLibraryService.class)
@EntityScan(basePackageClasses = DocumentLibraryEntity.class)
@EnableJpaRepositories(basePackageClasses = DocumentLibraryPersistence.class)
public class DocumentLibraryConfiguration {
}
