plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation("org.springframework:spring-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("software.amazon.awssdk:s3:2.40.15")

    runtimeOnly("com.mysql:mysql-connector-j")
}


tasks.bootJar {
    archiveFileName = "batch.jar"
}