plugins {
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // l10nmessages
    implementation("com.pinterest.l10nmessages:l10nmessages:0.0.9-SNAPSHOT")
    annotationProcessor("com.pinterest.l10nmessages:l10nmessages-proc:0.0.9-SNAPSHOT")
    annotationProcessor(files("src/main/resources"))

    // for tests - not needed by l10nmessages
    testImplementation(platform("org.junit:junit-bom:5.8.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}


application {
    mainClass.set("com.pinterest.l10nmessages.example.Application")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}