import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("native.cocoapods")
    id("com.vanniktech.maven.publish") version "0.28.0"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("release", "debug")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()
    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.0"
        podfile = project.file("../iosApp/Podfile") // why doesn't it load the cocoapods from the iosApp podfile?
        framework {
            baseName = "shared"
            isStatic = true
        }
        // Must define the pods that are in the Podfile (Is this just the way it works?)
        pod("PhoneNumberKit") {
            version ="3.7"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("libPhoneNumber-iOS") {
            version ="0.8"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            api(libs.androidx.activity.compose)
            api(libs.androidx.appcompat)
            //phone
            implementation(libs.libphonenumber)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

android {
    namespace = "io.github.lilytreasure"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.lilytreasure",
        artifactId = "multiplatformContacts",
        version = "1.0.1"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("MultiplatformContacts")
        description.set(
            "Kotlin Multiplatform library for Compose Multiplatform, " +
                    "designed for seamless integration of an contacts picker feature in iOS " +
                    "and Android applications.",
        )
        inceptionYear.set("2024")
        url.set("hhttps://github.com/Lilytreasure/MultiplatformContacts")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("dennis")
                name.set("dennis")
                email.set("lilyngure@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            connection.set("https://github.com/Lilytreasure/MultiplatformContacts.git")
            url.set("https://github.com/Lilytreasure/MultiplatformContacts")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}


