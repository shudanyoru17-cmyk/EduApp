# EduApp R8/ProGuard rules — Compose + Hilt + Room + Coroutines
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault
-keepattributes Signature,InnerClasses,EnclosingMethod

# Coroutines
-dontwarn kotlinx.coroutines.**
-keepclassmembers class kotlinx.coroutines.** { volatile <fields>; }

# Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.runtime.** { *; }

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep class **_HiltModules* { *; }
-keep class **_Factory { *; }
-keep class **_MembersInjector { *; }
-keep class hilt_aggregated_deps.** { *; }
-dontwarn dagger.hilt.**
-keep class com.edu.app.EduApplication { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * { @androidx.room.* <methods>; }
-keepclassmembers class com.edu.app.data.local.entity.** { *; }
-keepclassmembers class com.edu.app.data.local.relation.** { *; }
-keepclassmembers class com.edu.app.data.local.fts.** { *; }
-dontwarn androidx.room.paging.**

# Domain models + enums
-keep class com.edu.app.domain.model.** { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class com.edu.app.** {
    public ** component*();
    public ** copy(...);
}

# Navigation
-dontwarn androidx.navigation.**

# Strip verbose logs in release
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
}
