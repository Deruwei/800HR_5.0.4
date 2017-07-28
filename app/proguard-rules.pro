# To enable ProGuard in your project, edit project.properties
# to define the proguard.config property as described in that file.
#
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in ${sdk.dir}/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard
# include property in project.properties.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#########################################################################
#########################################################################
#-ignorewarnings
-dontskipnonpubliclibraryclassmembers
-keep class **.R$* {*;}
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class android.net.http.SslError
#########################################################################
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }  
-keep interface android.support.v4.app.** { *; }  
-keep public class * extends android.support.v4.**  
-keep public class * extends android.app.Fragment
#########################################################################
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
############################################################################
-dontwarn com.android.**
-keep class com.android.** { *; }
############################################################################
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }
############################################################################
#-keepattributes *Annotation*
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}
#-keepattributes Signature
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.examples.android.model.** { *; }
############################################################################

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
############################################################################
-keepattributes SourceFile,LineNumberTable
-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.**{ *; }
# ProGuard configurations for NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses
# End NetworkBench Lens
# ProGuard configurations for NetworkBench Lens
-keep class com.networkbench.** { *; }
-dontwarn com.networkbench.**
-keepattributes Exceptions, Signature, InnerClasses
# End NetworkBench Lens
-keepattributes SourceFile,LineNumberTable
############################################################################
-keep class com.lidroid.** { *; }
############################################################################
-keep class com.umeng.** { *; }
-keep class com.mob.** { *; }
-dontwarn com.mob.**
-dontwarn com.umeng.**
-dontwarn com.lidroid.**
############################################################################
-keep class cn.jpush.** { *; }
-dontwarn cn.jpush.**
-keep class com.google.** { *; }
-dontwarn com.google.**
############################################################################
-keep class com.novell.** { *; }
-dontwarn  com.novell.**
-keep class org.** { *; }
-dontwarn  org.**
-keep class de.measite.smack.** { *; }
-dontwarn   de.measite.smack.**
###########################################################
#用 butterknife插件 打包时要加上 !!!!!!!
#-dontwarn butterknife.internal.**
#-keep class **$$ViewInjector { *; }
#-keepnames class * { @butterknife.InjectView *;}
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class com.iflytek.** {*;}
-dontwarn com.iflytek.**
-keepattributes Signature

-keep class com.jph.takephoto.** { *; }
-dontwarn com.jph.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
