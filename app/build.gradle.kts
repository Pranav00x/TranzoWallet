plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tranzo.wallet"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tranzo.wallet"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ETHERSCAN_API_KEY", "\"${project.findProperty("ETHERSCAN_API_KEY") ?: ""}\"")
        buildConfigField("String", "COINGECKO_API_KEY", "\"${project.findProperty("COINGECKO_API_KEY") ?: ""}\"")
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DISCLAIMER"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
            // Pick first when both bcprov variants bundle the same resource files
            pickFirsts += "org/bouncycastle/**"
        }
    }

    configurations.all {
        // Exclude old BouncyCastle variants - keep only bcprov-jdk18on
        exclude(group = "org.bouncycastle", module = "bcprov-jdk15on")
        exclude(group = "org.bouncycastle", module = "bcprov-jdk15to18")
    }
}

dependencies {
    // Modules
    implementation(project(":core:crypto"))
    implementation(project(":core:keystore"))
    implementation(project(":core:security"))
    implementation(project(":data:network"))
    implementation(project(":data:repository"))
    implementation(project(":data:localdb"))
    implementation(project(":domain:model"))
    implementation(project(":domain:usecase"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:wallet"))
    implementation(project(":feature:tokens"))
    implementation(project(":feature:nft"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:payments"))
    implementation(project(":ui:frost-components"))
    implementation(project(":ui:theme"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.animation)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Security
    implementation(libs.security.crypto)
    implementation(libs.biometric)

    // Image
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.test.runner)
    debugImplementation(libs.compose.ui.test.manifest)
}
