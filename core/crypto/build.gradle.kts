plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tranzo.wallet.core.crypto"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

configurations.all {
    exclude(group = "org.bouncycastle", module = "bcprov-jdk15on")
}

dependencies {
    api(libs.web3j.core)
    implementation(libs.bitcoinj.core)
    implementation(libs.coroutines.core)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
