plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.22.3")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
