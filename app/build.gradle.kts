plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Android.compileSdk
    buildToolsVersion = Android.buildTools

    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(Modules.core))
    implementation(project(Modules.logging))
    implementation(project(Modules.wordFavouritesList))
    implementation(project(Modules.wordList))
    implementation(project(Modules.settings))

    implementation(Accompanist.animations)
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Google.material)

    implementation(Compose.activity)
    implementation(Compose.ui)
    implementation(Compose.material)
    debugImplementation(Compose.tooling)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigation)

    implementation(Hilt.android)
    kapt(Hilt.compiler)

    // need this for DI to work properly
    implementation(Retrofit.retrofit)
    implementation(OkHttp.loggingInterceptor)
    implementation(Room.runtime)


    implementation(AndroidX.lifecycleVmKtx)
    implementation(AndroidX.lifeCycleRuntime)
    implementation(AndroidX.composeViewModel)

    testImplementation(Junit.junit4)
    androidTestImplementation(Testing.androidXRunner)
    androidTestImplementation(ComposeTest.uiTestJunit4)
    androidTestImplementation(Testing.hiltAndroidTesting)
    kaptAndroidTest(Testing.hiltTestCompiler)
    androidTestImplementation(Junit.junit4)
    androidTestImplementation(Testing.androidxTestExt)
}