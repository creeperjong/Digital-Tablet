import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.digitaltablet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.digitaltablet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val keystoreFile = project.rootProject.file("apikeys.properties")
        if (keystoreFile.exists()) {
            val properties = Properties()
            properties.load(keystoreFile.inputStream())
            properties.forEach { (key, value) ->
                buildConfigField(
                    type = "String",
                    name = key.toString(),
                    value = value.toString()
                )
            }
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes.add("META-INF/LICENSE-LGPL-2.1.txt")
            excludes.add("META-INF/LICENSE-LGPL-3.txt")
            excludes.add("META-INF/LICENSE-W3C-TEST")
            excludes.add("META-INF/DEPENDENCIES")
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

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // CameraX
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Zxing
    implementation(libs.google.core)

    // Jetpack Compose integration
    implementation(libs.navigation.compose)

    // Dagger Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // Paho MQTT
    implementation(libs.org.eclipse.paho.client.mqttv3)
    // Solve MqttAndroidClient obsolete dependency
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.paho.mqtt.android)

    // Exoplayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Reflection
    implementation(libs.kotlin.reflect)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // QRCode scanner
    implementation(libs.play.services.mlkit.barcode.scanning)

    // Markdown
    implementation(libs.flexmark.all)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

kapt {
    correctErrorTypes = true
}