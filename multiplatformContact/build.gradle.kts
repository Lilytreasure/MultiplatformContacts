import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("native.cocoapods")
    id("com.vanniktech.maven.publish") version "0.28.0"
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.swiftKlib)
}

swiftklib {
    create("ContactsHelper") {
        path = file("/Users/admin/StudioProjects/MultiplatformContacts/iosApp/iosApp/contacts")
        packageName("io.github.lilytreasure")
        minIos = 14
    }
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

    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    iosTargets.forEach { target ->
        target.compilations["main"].cinterops {
            create("ContactsHelper") {
                defFile(project.file("src/iosMain/cinterop/ContactsHelper.def"))
                packageName("io.github.lilytreasure")
            }
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.0"
        podfile = project.file("../iosApp/Podfile") // ✅ This will load your Podfile correctly
        framework {
            baseName = "shared"
            isStatic = true
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


