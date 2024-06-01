plugins {
    id("java")
}

group = "com.fgiannesini"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

dependencies {
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    testAnnotationProcessor ("org.openjdk.jmh:jmh-generator-annprocess:1.37")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Test> {
    jvmArgs("--add-modules", "jdk.incubator.vector")
}

tasks.withType<JavaExec> {
    jvmArgs("--add-modules", "jdk.incubator.vector")
}