# KinderHub ProGuard Rules

# Keep Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.kinderhub.ui.**$$serializer { *; }
-keepclassmembers class com.kinderhub.ui.** {
    *** Companion;
}
-keepclasseswithmembers class com.kinderhub.ui.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**
