plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.todolist"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.todolist"
        minSdk = 24
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    /*
    kapt {
        correctErrorTypes = true
    }

     */

}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    // To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha13")
    implementation("androidx.compose.foundation:foundation-android:1.6.8")

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("com.google.accompanist:accompanist-themeadapter-material3:0.28.0")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.6.8")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.hilt:hilt-common:1.2.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.work:work-runtime-ktx:2.7.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    testImplementation("io.mockk:mockk:1.13.12")
    //  testImplementation("org.mockito:mockito-core:2.19.0")

    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Для Turbine
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")

    // Для Kotlin Coroutines
    //testImplementation("io.mockk:mockk:2.19.0")
    // Для тестирования Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")
    androidTestImplementation("androidx.compose.ui:ui-test-manifest:1.6.8")
    androidTestImplementation("androidx.compose.ui:ui-test:1.6.8")

//
// testImplementation(libs.androidx.ui.test.junit4)
//    testImplementation(libs.mockk)
//    testImplementation(libs.ktor.mock)
//    androidTestImplementation(libs.androidx.junit.ktx)
//    androidTestImplementation(libs.androidx.test.runner)

    // Для Hilt
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.51.1")

    androidTestImplementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Для unit-тестов
    //testImplementation("org.mockito:mockito-core:4.0.0")

    // Для UI-тестов с Jetpack Compose
    //androidTestImplementation("androidx.compose.ui:ui-test-manifest:1.6.8")

    // MockWebServer для тестирования сетевых вызовов
    //androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    //kapt("groupId:artifactId:version")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("com.yandex.div:div-core:3.0.1")
    implementation("com.yandex.div:div:3.0.1")
    implementation("com.yandex.div:div-json:3.0.1")
    implementation("com.squareup.picasso:picasso:2.8")
}
