plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mentorship.catalog"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }
}

dependencies {
    implementation(project(":catalog:details"))
    implementation(project(":core"))
}
