# Tranzo Wallet ProGuard Rules

# Web3j
-keep class org.web3j.** { *; }
-dontwarn org.web3j.**

# Bitcoinj
-keep class org.bitcoinj.** { *; }
-dontwarn org.bitcoinj.**

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Keep data models
-keep class com.tranzo.wallet.domain.model.** { *; }
-keep class com.tranzo.wallet.data.localdb.entity.** { *; }
-keep class com.tranzo.wallet.data.network.** { *; }

# BouncyCastle
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**
