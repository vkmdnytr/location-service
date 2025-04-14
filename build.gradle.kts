// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
		repositories {
				google()
				mavenCentral()
		}
}

plugins {
		alias(libs.plugins.androidApplication) apply false
		alias(libs.plugins.androidLibrary) apply false
		alias(libs.plugins.kotlinAndroid) apply false
		alias(libs.plugins.hilt) apply false
}

tasks.register("clean", Delete::class) {
		delete(layout.buildDirectory)
}