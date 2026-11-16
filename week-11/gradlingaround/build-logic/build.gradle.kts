plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("exportTranslations") {
            id = "com.mentorship.translations.export"
            implementationClass = "ExportTranslationsPlugin"
        }
        register("importTranslations") {
            id = "com.mentorship.translations.import"
            implementationClass = "ImportTranslationsPlugin"
        }
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}