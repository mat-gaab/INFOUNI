plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.exemplo.infouni"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.exemplo.infouni"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Controla as versões do Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.18.0"))
    // Biblioteca para o Banco de Dados (Realtime Database)
    implementation("com.google.firebase:firebase-database")
    // Biblioteca para a IA (Gemini)
    implementation("com.google.firebase:firebase-ai")
    // Corrotinas para não travar a tela durante buscas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Biblioteca para carregar imagens da web
    implementation("com.github.bumptech.glide:glide:4.16.0")
}