-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.karlsj.communitypantryfinder.**$$serializer { *; }
-keepclassmembers class om.karlsj.communitypantryfinder.** {
    *** Companion;
}
-keepclasseswithmembers class om.karlsj.communitypantryfinder.** {
    kotlinx.serialization.KSerializer serializer(...);
}