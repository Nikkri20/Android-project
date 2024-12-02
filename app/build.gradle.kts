plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.android_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_project"
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
    // ------------------------------
    // Основные зависимости Android и Jetpack Compose
    // ------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // ------------------------------
    // Material Design
    // ------------------------------
    implementation(libs.androidx.material3.v110)

    // ------------------------------
    // Архитектура и управление состоянием
    // ------------------------------
    implementation(libs.androidx.lifecycle.viewmodel.compose.v261)
    implementation(libs.androidx.lifecycle.runtime.compose.v261)
    implementation(libs.kotlinx.coroutines.core.v164)
    implementation(libs.kotlinx.coroutines.android.v164)

    // ------------------------------
    // Навигация
    // ------------------------------
    implementation(libs.androidx.navigation.compose)

    // ------------------------------
    // ConstraintLayout для Compose
    // ------------------------------
    implementation(libs.androidx.constraintlayout.compose.android)

    // ------------------------------
    // Работа с сохранением состояния
    // ------------------------------
    implementation(libs.runtime.saveable.v143)

    // ------------------------------
    // Работа с данными
    // ------------------------------
    implementation(libs.androidx.datastore.preferences)
    implementation("androidx.room:room-runtime:2.5.2")
    implementation(libs.navigation.runtime.ktx)
    ksp("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    // ------------------------------
    // Зависимости Hilt
    // ------------------------------
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // ------------------------------
    // Сетевые библиотеки
    // ------------------------------
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.squareup.converter.scalars)

    // ------------------------------
    // Загрузка изображений
    // ------------------------------
    implementation(libs.coil.compose.v240)

    // ------------------------------
    // HTML парсинг
    // ------------------------------
    implementation(libs.jsoup)

    // ------------------------------
    // Тестирование
    // ------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // ------------------------------
    // Зависимости для отладки
    // ------------------------------
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}



