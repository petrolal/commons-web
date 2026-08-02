# Commons Web

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.10-purple.svg)](https://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://oracle.com/java)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-green.svg)](https://spring.io/projects/spring-boot)

`commons-web` is a foundational Spring Boot auto-configuration library written in Kotlin for microservices and web applications. It provides pre-configured defaults and shared abstractions for correlation ID tracing, global exception handling, JPA auditing, Flyway migrations, OpenAPI documentation, and HATEOAS hypermedia support.

---

## Features

- **Correlation ID Tracing**: Automatic HTTP request filtering with MDC logging context (`X-Correlation-ID`).
- **Global Exception Handling**: Standardized REST error response model (`ErrorResponse`) with automatic validation handling (`MethodArgumentNotValidException`).
- **JPA & Auditing Abstractions**: Abstract `BaseEntity` with auto-managed creation/update timestamps (`created_at`, `updated_at`), ID generation, optimistic locking, and pre-enabled JPA auditing.
- **Flyway Auto-Configuration**: Default database migration configuration with `baselineOnMigrate` support.
- **OpenAPI / Swagger Docs**: Pre-configured `OpenAPI` bean including automatic HTTP correlation header parameters in Swagger UI.
- **HATEOAS Utilities**: Abstract model assemblers (`BaseModelAssembler`) and base representation models (`BaseRepresentationModel`) pre-configured with Spring HATEOAS HAL support.

---

## Installation

### Gradle (Kotlin DSL)

Add the GitHub Packages repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/petrolal/commons-web")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: "petrolal"
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("com.petrolal.commons.web:commons-web:2.2.0")
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/petrolal/commons-web</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.petrolal.commons.web</groupId>
    <artifactId>commons-web</artifactId>
    <version>2.2.0</version>
</dependency>
```

---

## Configuration

Customize behavior using standard Spring Boot `application.yml` or `application.properties`:

```yaml
commons:
  web:
    correlation-header-name: "X-Correlation-ID" # Custom HTTP correlation header name
    swagger:
      enabled: true
      title: "My Service API"
      description: "Service endpoints documentation"
      version: "1.0.0"
    jpa:
      enable-auditing: true # Enables Spring Data JPA Auditing (@CreatedDate, @LastModifiedDate)
    flyway:
      enabled: true
      locations:
        - "classpath:db/migration"
```

---

## Usage Guide

### 1. Base Entity & JPA Auditing

Extend `BaseEntity` in your JPA domain entities to automatically receive primary key generation (`id`), created/updated timestamps (`created_at`, `updated_at`), and optimistic versioning (`version`):

```kotlin
import com.petrolal.commons.web.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    var name: String,
    var price: Double
) : BaseEntity()
```

### 2. Correlation ID Tracing

The `CorrelationIdFilter` automatically intercepts incoming HTTP requests:
- Reads the correlation header (`X-Correlation-ID` by default).
- Generates a UUID if the header is missing.
- Places `correlationId` into SLF4J MDC for logging contexts.
- Echoes the correlation ID back in the HTTP response headers.

Example log format configuration (`logback.xml` or `application.yml`):
```yaml
logging:
  pattern:
    level: "%5p [correlationId: %X{correlationId}]"
```

### 3. Standardized Error Handling

Uncaught exceptions and validation errors return a consistent JSON payload format (`ErrorResponse`):

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "timestamp": "2026-07-26T20:30:00Z",
  "details": [
    "name: must not be blank",
    "price: must be greater than zero"
  ]
}
```

### 4. HATEOAS Assemblers

Use `BaseModelAssembler` to transform domain entities into HATEOAS representation models:

```kotlin
import com.petrolal.commons.web.hateoas.BaseModelAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

data class ProductDto(val id: Long?, val name: String, val price: Double)

@Component
class ProductModelAssembler : BaseModelAssembler<Product, ProductDto>() {
    override fun toResourceDto(entity: Product): ProductDto =
        ProductDto(entity.id, entity.name, entity.price)

    override fun addLinks(entity: Product, model: EntityModel<ProductDto>) {
        model.add(linkTo(methodOn(ProductController::class.java).getById(entity.id!!)).withSelfRel())
    }
}
```

---

## Development & Building

### Requirements
- Java 21 JDK
- Gradle 8+ (or using `./gradlew` wrapper)

### Commands

- **Build project**:
  ```bash
  ./gradlew build
  ```

- **Run tests**:
  ```bash
  ./gradlew test
  ```

- **Publish package**:
  ```bash
  ./gradlew publish
  ```

---

## License

This project is licensed under the MIT License.
