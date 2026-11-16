plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mentorship.profile.settings"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }
}
