plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mentorship.profile"
    compileSdk = 37

    defaultConfig {
        minSdk = 31
    }
}

dependencies {
    implementation(project(":profile:settings"))
    implementation(project(":core"))
}
