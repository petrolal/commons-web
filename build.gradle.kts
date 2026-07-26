plugins {
    alias(libs.plugins.kotlin.main)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    `java-library`
    `maven-publish`
}

group = "com.petrolal.commons.web"
version =  "2.4.0"

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

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/petrolal/spring-commons-web")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: "petrolal"
                password = System.getenv("GITHUB_TOKEN") ?: System.getenv("GH_PAT")
            }
        }
    }
}