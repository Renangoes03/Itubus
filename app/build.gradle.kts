plugins {
    ("com.google.gms.google-services")

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.viajet.itubus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.viajet.itubus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.dataconnect)
    implementation(libs.datastore.core.android)
    implementation(libs.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Jetpack Compose testing dependencies
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Custom libraries
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // FirebaseUI Storage only
    implementation ("com.firebaseui:firebase-ui-storage:7.2.0")

    implementation ("androidx.cardview:cardview:1.0.0")

    //Import OneSignal
    implementation ("com.onesignal:OneSignal:4.8.4") // Verifique a versão mais recente

    implementation ("com.google.firebase:firebase-messaging:23.0.7") // Verifique a versão mais recente

    //QR Code
    implementation ("com.google.zxing:core:3.5.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    //implementation ("androidx.activity:activity-ktx:1.8.0")
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

     //OpenStreetMap
    implementation ("org.osmdroid:osmdroid-android:6.1.12")
    implementation ("com.google.zxing:core:3.4.1")

}

