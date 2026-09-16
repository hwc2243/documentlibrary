# Thymeleaf host integration example

This directory shows how a consuming Spring Boot application can apply its own
layout and theme to the document-library browser without changing the
document-library controller or services.

Copy `src/main/resources` into the host application. The host's classpath
templates take precedence over the templates packaged by the document-library
JAR:

- `templates/documentlibrary/index.html` replaces the document-library page shell.
- `templates/documentlibrary/fragments/*.html` supply the themed UI pieces.
- `templates/layout.html` represents the host application's shared layout.
- `static/css/document-library-host.css` is host-owned styling.

The controller exposes these model attributes to the templates:

| Attribute | Available when | Purpose |
| --- | --- | --- |
| `baseUrl` | always | configured document-library base URL |
| `libraries` | library root | libraries to display |
| `library` | library or folder view | selected library |
| `folder` | folder view | selected folder |
| `folderBreadcrumbs` | folder view | root-to-current folder trail |
| `folders` | library or folder view | folders at this location |
| `fileRows` | library or folder view | files and their versions |
| `success`, `error` | after a command | flash messages |

The forms and links deliberately keep the document-library endpoints. A host
application can add its own CSRF fields, authorization-aware controls, or
layout fragments as required by its security and design systems.
