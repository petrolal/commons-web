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
version = System.getenv("VERSION")

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
    api(libs.spring.boot.starter)
    api(libs.spring.boot.starter.web)
    api(libs.spring.boot.starter.validation)
    api(libs.spring.boot.starter.actuator)
    api(libs.spring.boot.starter.data.jpa)
    api(libs.spring.boot.starter.hateoas)
    api(libs.flyway.core)
    api(libs.flyway.database.postgresql)
    api(libs.springdoc.openapi.ui)
    api(libs.jackson.kotlin)
    api(libs.kotlin.reflect)
    runtimeOnly(libs.postgresql)

    constraints {
        implementation("org.apache.commons:commons-lang3:3.18.0") {
            because("Fixes CVE-2025-48924 transitive vulnerability")
        }
    }

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.h2)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
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