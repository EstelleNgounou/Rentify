// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()         // Google's Maven repository
        mavenCentral()   // Maven Central repository

    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")  // Use the latest stable version of the AGP
        // You can add other classpath dependencies here
        classpath ("com.google.gms:google-services:4.4.2")
    }
}

plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

allprojects {
    repositories {
        google()         // Google's Maven repository
        mavenCentral()   // Maven Central repository
    }
}

