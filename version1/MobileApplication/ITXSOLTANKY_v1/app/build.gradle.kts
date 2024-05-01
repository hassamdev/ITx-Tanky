plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.itx_soltanky"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.itx_soltanky"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.airbnb.android:lottie:6.2.0")
    implementation("com.github.AtifSayings:Animatoo:1.0.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("org.json:json:20210307")
//    implementation ("javax.websocket:javax.websocket-api:1.1")
//    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:2.1.4") {
////        exclude(group = "javax.websocket", module = "javax.websocket-api")
//    }


}