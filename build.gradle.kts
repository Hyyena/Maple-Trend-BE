import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    `java-library`
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "com.mapletrend"
    version = "0.0.1-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_17

    configurations {
        all {
            // Logback 의존성 제외
            exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.register<Copy>("copySecrets") {
        from("../secrets/")
        into("src/main/resources/copied-secrets")
    }

    tasks.named<ProcessResources>("processResources") {
        dependsOn("copySecrets")
    }
}

project("common") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project("nexon-open-api-core") {
    dependencies {
        implementation(project(":common"))

        implementation("org.springframework.boot:spring-boot-starter-log4j2")

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.retry:spring-retry")

        implementation("com.googlecode.json-simple:json-simple:1.1.1")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}

project("app-maple-stamp-api") {
    dependencies {
        implementation(project(":common"))
        implementation(project(":nexon-open-api-core"))
        implementation(project(":maple-stamp-domain-mariadb"))

        implementation("org.springframework.boot:spring-boot-starter-log4j2")
        implementation("com.googlecode.json-simple:json-simple:1.1.1")

        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")

        implementation("io.micrometer:micrometer-registry-prometheus")

        developmentOnly("org.springframework.boot:spring-boot-devtools")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = true
    jar.enabled = false

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project("maple-stamp-domain-mariadb") {
    apply {
        plugin("java-library")
    }

    dependencies {
        implementation(project(":common"))

        api("org.springframework.boot:spring-boot-starter-data-jpa")

        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-jdbc")
        runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    }

    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.named<BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}
