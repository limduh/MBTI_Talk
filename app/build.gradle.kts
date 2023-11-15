plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")




}

android {
    namespace = "nb_.mbti_talk"
    compileSdk = 34

    defaultConfig {
        applicationId = "nb_.mbti_talk"
        minSdk = 26
        targetSdk = 33
        versionCode = 5
        versionName = "1.4"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }



    // Android 4.0 ~
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation ("com.github.ybq:Android-SpinKit:1.4.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:22.2.0") // 원래는 22.1.2 이나 도연 프로젝트에서는 22.2.0 으로만 빌드됨.
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.firebaseui:firebase-ui-storage:8.0.2") //glideModule 때문에 추가
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //바텀네비게이션바 의존성추가
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation ("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")

    // design round image view
    implementation("com.makeramen:roundedimageview:2.3.0")

    // gson converter
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //google로그인
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    //Glide 라이브러리, Glide 컴파일러
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation(project(":wavybottomnavigation"))
}
