plugins {
    id("com.android.application")
}

android {
    namespace = "ma.tp.calculatrice"
    compileSdk = 34

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    defaultConfig {
        applicationId = "ma.tp.calculatrice"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    testImplementation ("org.robolectric:robolectric:4.6.1");
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    implementation ("io.apisense:rhino-android:1.0")
    implementation ("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
    implementation("androidx.test:runner:1.4.0")
    implementation("androidx.test.ext:junit:1.1.3")
    testImplementation ("org.robolectric:robolectric:4.3.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:3.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}