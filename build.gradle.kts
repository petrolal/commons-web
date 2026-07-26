plugins {
    alias(libs.plugins.kotlin.main)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    `java-library`
    `maven-publish`
}

group = "com.petrolal.spring.commons.web"
version = "1.0.0"

java {
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
    api(libs.springdoc.openapi.ui)
    api(libs.spring.boot.starter.hateoas)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)

    constraints {
        implementation("org.apache.commons:commons-lang3:3.18.0") {
            because("Fixes CVE-2025-48924 transitive vulnerability")
        }
    }

    testImplementation(libs.spring.boot.starter.test)
}

tasks.named<Jar>("jar") {
    enabled = true
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/petrolal/spring-commons-web")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: project.findProperty("gpr.user") as String?
                password = System.getenv("GITHUB_TOKEN") ?: project.findProperty("gpr.key") as String?
            }
        }
    }
}