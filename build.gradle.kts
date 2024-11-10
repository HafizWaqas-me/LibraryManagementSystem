// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.7.2" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
}
buildscript{
    dependencies{
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.23")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.3")
        classpath("com.google.gms:google-services:4.4.2")

    }
}