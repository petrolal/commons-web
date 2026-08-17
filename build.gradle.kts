plugins {
    alias(libs.plugins.kotlin.main)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.vannitech.maven.publish)
    `java-library`
    `maven-publish`
}

group = "com.petrolal.commons.web"
version = project.findProperty("version")?.toString()?.takeIf { it.isNotBlank() && it != "unspecified" }
    ?: System.getenv("VERSION")?.takeIf { it.isNotBlank() }
    ?: "2.4.0"

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    api(libs.spring.boot.starter.web)
    api(libs.spring.boot.starter.data.jpa)
    api(libs.spring.boot.starter.hateoas)
    api(libs.flyway.core)
    api(libs.flyway.database.postgresql)
    api(libs.springdoc.openapi.ui)
    api(libs.jackson.kotlin)
    api(libs.kotlin.reflect)
    api(libs.spring.boot.devtools)
    api(libs.spring.boot.starter.test)
    api(libs.junit.platform.launcher)
    api(libs.h2)
    api(libs.google.api.client)
    api(libs.google.oauth.client.jetty)
    api(libs.google.api.services.sheets)
    api(libs.google.http.client.jackson2)
    api(libs.spring.boot.docker.compose)
    api(libs.spring.boot.testcontainers)
    api(libs.testcontainers.postgresql)
    api(libs.testcontainers.junit)
    runtimeOnly(libs.postgresql)

    constraints {
        implementation("org.apache.commons:commons-lang3:3.18.0") {
            because("Fixes CVE-2025-48924 transitive vulnerability")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    enabled = true
}

val hasSigningKey = project.hasProperty("signingInMemoryKey") ||
    project.hasProperty("signing.keyId") ||
    project.hasProperty("signing.gnupg.keyName") ||
    System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey") != null ||
    System.getenv("SIGNING_KEY") != null ||
    System.getenv("GPG_SIGNING_KEY") != null

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    if (hasSigningKey) {
        signAllPublications()
    }

    coordinates(group.toString(), "commons-web", version.toString())

    pom {
        name.set("commons-web")
        description.set("A commons Web Library for Kotlin with Spring")
        url.set("https://github.com/petrolal/commons-web")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("petrolal")
                name.set("Lucas Petrola")
                email.set("petrolalucas@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/petrolal/commons-web.git")
            developerConnection.set("scm:git:ssh://github.com:petrolal/commons-web.git")
            url.set("https://github.com/petrolal/commons-web")
        }
    }
}
