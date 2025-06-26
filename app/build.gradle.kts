plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.geopet"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.geopet"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.lifecycle.runtime.ktx.v290)
    implementation(libs.play.services.location)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation (libs.accompanist.permissions)
    implementation(libs.androidx.navigation.compose)
    implementation (libs.maps.compose)
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material.icons.extended)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    // Retrofit
    implementation(libs.retrofit.v290)
    // Retrofit with Scalar Converter
    implementation(libs.converter.scalars)

    implementation(libs.coil.compose.v250)
    implementation(libs.glide)
    implementation(libs.coil.kt.coil.compose)
    //Firebase
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.play.services.auth)
    implementation (libs.firebase.firestore)
    implementation (libs.play.services.auth.v2100)
    implementation (libs.google.firebase.auth.ktx)

    implementation (libs.com.google.firebase.firebase.auth.ktx)
    implementation (libs.kotlinx.coroutines.play.services)


}