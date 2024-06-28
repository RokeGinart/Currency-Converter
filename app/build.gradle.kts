plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.currencyconverter"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.currencyconverter"
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
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"https://developers.paysera.com/tasks/api/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

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
        buildConfig = true
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

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

    // Android X
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-service:2.8.2")
    implementation("androidx.lifecycle:lifecycle-process:2.8.2")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Compose
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material:1.7.0-beta03")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Moshi
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
    //androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
   // androidTestImplementation("androidx.compose.ui:ui-test-junit4")
   // debugImplementation("androidx.compose.ui:ui-tooling")
    //debugImplementation("androidx.compose.ui:ui-test-manifest")
}