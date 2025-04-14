plugins {
		alias(libs.plugins.androidApplication)
		alias(libs.plugins.kotlinAndroid)
		alias(libs.plugins.hilt)
		kotlin("kapt")
}

android {
		namespace = "com.marti.map"
		compileSdk = 34

		defaultConfig {
				applicationId = "com.marti.map"
				minSdk = 24
				targetSdk = 34
				versionCode = 1
				versionName = "1.0"

				testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
				vectorDrawables {
						useSupportLibrary = true
				}
		}

		buildTypes {
				release {
						isMinifyEnabled = false
						proguardFiles(
								getDefaultProguardFile("proguard-android-optimize.txt"),
								"proguard-rules.pro"
						)
				}
		}
		compileOptions {
				sourceCompatibility = JavaVersion.VERSION_1_8
				targetCompatibility = JavaVersion.VERSION_1_8
		}
		kotlinOptions {
				jvmTarget = "1.8"
		}
		buildFeatures {
				compose = true
		}
		composeOptions {
				kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
		}
		packaging {
				resources {
						excludes += "/META-INF/{AL2.0,LGPL2.1}"
				}
		}
}

dependencies {

		implementation(libs.core.ktx)
		implementation(libs.lifecycle.runtime.ktx)
		implementation(libs.activity.compose)
		implementation(platform(libs.compose.bom))
		implementation(libs.ui)
		implementation(libs.ui.graphics)
		implementation(libs.ui.tooling.preview)
		implementation(libs.material3)
		implementation(libs.navigation.compose)
		implementation(libs.hilt.android)
		implementation(libs.hilt.navigation.compose)
		implementation(libs.androidx.lifecycle.service)
		kapt(libs.hilt.compiler)
		testImplementation(libs.junit)
		androidTestImplementation(libs.androidx.test.ext.junit)
		androidTestImplementation(libs.espresso.core)
		androidTestImplementation(platform(libs.compose.bom))
		androidTestImplementation(libs.ui.test.junit4)
		debugImplementation(libs.ui.tooling)
		debugImplementation(libs.ui.test.manifest)

		implementation(libs.maps.compose)
		implementation(libs.play.services.maps)
		implementation(libs.play.services.location)
		implementation(libs.accompanist.permissions)
}