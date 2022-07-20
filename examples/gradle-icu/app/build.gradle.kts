plugins {
    application
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("com.pinterest.l10nmessages:l10nmessages:0.0.9-SNAPSHOT")
    implementation("com.ibm.icu:icu4j:70.1")
    annotationProcessor("com.pinterest.l10nmessages:l10nmessages-proc:0.0.9-SNAPSHOT")
    annotationProcessor("com.ibm.icu:icu4j:70.1")
    annotationProcessor(files("src/main/resources"))
}


application {
    mainClass.set("com.pinterest.l10nmessages.example.Application")
}
