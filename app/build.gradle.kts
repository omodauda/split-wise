import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.crashlytics)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

val properties = Properties().apply {
    val propertiesFile = rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
    }
}

android {
    namespace = "com.omodauda.splitwise"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.omodauda.splitwise"
        minSdk = 26
        targetSdk = 36
        versionCode = 4
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // RELEASE BASE URL
            val baseUrl = properties.getProperty("BASE_URL")
            buildConfigField("String", "BASE_URL", baseUrl)

            val googleServerClientId = properties.getProperty("GOOGLE_SERVER_CLIENT_ID")
            buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", googleServerClientId)
        }

        getByName("debug") {
            isMinifyEnabled = false
            // DEBUG BASE URL
            val baseUrl = properties.getProperty("BASE_URL")
            buildConfigField("String", "BASE_URL", baseUrl)

            val googleServerClientId = properties.getProperty("GOOGLE_SERVER_CLIENT_ID")
            buildConfigField("String", "GOOGLE_SERVER_CLIENT_ID", googleServerClientId)
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
        buildConfig = true
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
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.retrofit)
    implementation(libs.gsonConverter)
    implementation(libs.okHttp)
    implementation(libs.okHttpLogging)
    implementation(libs.androidx.security.crypto)
    implementation(libs.compose.shimmer)
    implementation(libs.androidx.paging)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
