# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html

-dontusemixedcaseclassnames
-verbose

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Keep the line numbers and file name attributes
-renamesourcefileattribute SourceFile

# Preserve all native method names and the names of their classes
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve custom application classes
-keep public class com.pramod.octapadpromidi.** {
    public *;
    protected *;
}

# Keep R classes
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep view constructors for inflation from XML
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep fragment classes
-keep public class * extends androidx.fragment.app.Fragment

# Keep activity classes
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# Preserve annotations
-keepattributes *Annotation*

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
