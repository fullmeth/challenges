plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mentorship.core.ui"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }
}
