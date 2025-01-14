import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {

    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "tn.iptv.nextplayer.feature.player"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    dataBinding{
        enable = true
    }


    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.android.jvm.get().toInt())
        targetCompatibility = JavaVersion.toVersion(libs.versions.android.jvm.get().toInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.android.jvm.get()
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewModel.ktx)
    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.reftroit)
    implementation(libs.reftroit.converter.moshi)
    implementation(libs.reftroit.converter.gson)
    implementation(libs.bumptech.glide)
    implementation(libs.circularimageview)


    // Media3
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.exoplayer.rtsp)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)
    implementation(libs.github.anilbeesetti.nextlib.media3ext)
    implementation(libs.github.anilbeesetti.nextlib.mediainfo)



    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.activity)
    ksp(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.compiler)

    implementation(libs.timber)

    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
