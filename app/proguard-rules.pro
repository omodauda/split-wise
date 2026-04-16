# 1. General App Security
#-----------------------------------------------------------------------------------------
# We do NOT add a -keep rule for BuildConfig. This allows R8 to obfuscate
# the BASE_URL and other config fields, making them harder to find in a decompile.

# Preserve line numbers in crash reports (highly recommended for production)
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 2. Retrofit & OkHttp
# -----------------------------------------------------------------------------------------
# Retain annotations used by Retrofit (e.g., @GET, @POST, @Body)
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Prevent OkHttp from being stripped or broken
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# 3. Gson (Critical for your Network Models)
# -----------------------------------------------------------------------------------------
# Gson uses reflection. If R8 renames your data classes (like ActivityItem),
# Gson won't be able to map JSON keys to your fields.
# We keep all classes in your model package:
-keep class com.omodauda.splitwise.data.network.model.** { *; }

# Also keep the @SerializedName annotation
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# 4. Paging 3 & DataStore
# -----------------------------------------------------------------------------------------
-keep class androidx.paging.** { *; }
-dontwarn androidx.datastore.**

# 5. Firebase Messaging
# -----------------------------------------------------------------------------------------
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**