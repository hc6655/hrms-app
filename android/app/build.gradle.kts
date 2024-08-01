ext {
    set("koin_version", "3.5.0")
    set("ktor_version", "2.3.7")
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.hkbufyp.hrms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hkbufyp.hrms"
        minSdk = 26
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
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.31.0-alpha")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("com.kizitonwose.calendar:view:2.5.0")
    implementation("com.kizitonwose.calendar:compose:2.5.0")
    implementation("com.seanproctor:data-table-material3:0.5.1")

    // koin
    implementation("io.insert-koin:koin-core:${project.ext.get("koin_version")}")
    implementation("io.insert-koin:koin-android:${project.ext.get("koin_version")}")
    implementation("io.insert-koin:koin-androidx-compose:${project.ext.get("koin_version")}")
    implementation("io.insert-koin:koin-ktor:${project.ext.get("koin_version")}")
    implementation("io.insert-koin:koin-logger-slf4j:${project.ext.get("koin_version")}")

    // ktor
    implementation("io.ktor:ktor-client-core:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-client-cio:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-client-android:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-client-serialization:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-client-logging:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-client-content-negotiation:${project.ext.get("ktor_version")}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${project.ext.get("ktor_version")}")
    implementation("com.google.firebase:firebase-messaging:23.4.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}