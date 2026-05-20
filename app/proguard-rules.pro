# 1. General App Security
# -----------------------------------------------------------------------------------------
# We do NOT add a -keep rule for BuildConfig. This allows R8 to obfuscate
# the BASE_URL and other config fields, making them harder to find in a decompile.

# Preserve line numbers in crash reports (highly recommended for production)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 2. Retrofit & OkHttp
# -----------------------------------------------------------------------------------------
# Keep generic signatures so Retrofit knows the types inside List<T> or Response<T>
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# Retain Retrofit's own classes and methods
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Prevent OkHttp from being stripped or broken
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# 3. Gson / Data Models
# -----------------------------------------------------------------------------------------
# Gson uses reflection. If R8 renames your data classes or their fields,
# Gson won't be able to map JSON keys correctly.
# We keep all classes in your model package:
-keep class com.omodauda.splitwise.data.network.model.** { *; }

# Keep SerializedName annotations
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep Enum members for type conversion (e.g., ActivityType)
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 4. Paging 3, DataStore & Hilt
# -----------------------------------------------------------------------------------------
-keep class androidx.paging.** { *; }
-dontwarn androidx.datastore.**

# Hilt/Dagger reflection rules
-keep class dagger.hilt.android.internal.** { *; }

# 5. Firebase (Messaging, Analytics, Crashlytics)
# -----------------------------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# 6. Android Security Crypto (Specifically for version 1.0.0)
# -----------------------------------------------------------------------------------------
-keep class androidx.security.crypto.** { *; }
-dontwarn androidx.security.crypto.**

# 7. Credentials & Google ID (Fixes Sign-in failures in Proguard/R8 builds)
# -----------------------------------------------------------------------------------------
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }
-dontwarn androidx.credentials.**
