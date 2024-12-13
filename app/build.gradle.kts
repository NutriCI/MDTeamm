plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.nutlicii"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nutlicii"
        minSdk = 27
        targetSdk = 34
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.gridlayout)
    implementation (libs.androidx.cardview)
    implementation (libs.okhttp)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    kapt (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.fragment.ktx)
    implementation(libs.material.v190)
    implementation (libs.generativeai)
    implementation (libs.picasso)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)












}