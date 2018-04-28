# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile


# Hide duplicate class note
# https://stackoverflow.com/a/35742739/2708768
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**

# Retrofit:
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


# MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }

-keep class android.support.v7.widget.SearchView { *; }