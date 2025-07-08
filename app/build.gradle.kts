import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
}

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
android {
    namespace = "com.integragames.goldenchain"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dts.freefireth"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "USERID_CLASSNAME", apikeyProperties["USERID_CLASSNAME"] as String)
        buildConfigField("String", "USERID_INFO", apikeyProperties["USERID_INFO"] as String)
        buildConfigField("String", "USERID_METHOD", apikeyProperties["USERID_METHOD"] as String)
        buildConfigField("String", "REF", apikeyProperties["REF"] as String)
        buildConfigField("String", "DEEP", apikeyProperties["DEEP"] as String)
        buildConfigField("String", "GAID", apikeyProperties["GAID"] as String)
        buildConfigField("String", "UID", apikeyProperties["UID"] as String)
        buildConfigField("String", "HELPER", apikeyProperties["HELPER"] as String)
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Navigation destinations
    implementation(libs.animations.core)
    ksp(libs.ksp)
    implementation(libs.material3)
    implementation(libs.androidx.foundation)
    // OneSignal
    implementation(libs.one.signal)
    //Serialization
    implementation (libs.kotlinx.serialization.json)
    // Referrer
    implementation (libs.installreferrer)
    implementation(libs.play.services.ads.identifier)
}