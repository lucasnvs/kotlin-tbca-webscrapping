plugins {
    kotlin("jvm") version "1.9.21"
}

group = "com.lucasnvs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fleeksoft.ksoup:ksoup:0.1.2")
    implementation("org.ktorm:ktorm-core:4.0.0")
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("com.zaxxer:HikariCP:5.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}