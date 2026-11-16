plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mentorship.core"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }
}

dependencies {
    implementation(project(":core:ui"))
}
