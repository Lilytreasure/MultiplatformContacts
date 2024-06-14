

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.lilytreasure/multiplatformContacts"><img alt="Profile" src="https://badgen.net/badge/Maven Central/v1.0.0/blue?icon=github"/></a>
</p><br>
[![Maven Central](https://img.shields.io/maven-central/v/io.github.lilytreasure/multiplatformContacts?color=orange)](https://search.maven.org/search?q=g:io.lilytreasure)

<p align="center">
👻 Multiplatform Contacts is a straight forward library used to get  Contacts in Android and iOS.
</p><br>

### On Android

Add the following on your Manifest file:
```xml
  <uses-permission android:name="android.permission.READ_CONTACTS" />
```

### Gradle

You can add a dependency inside the `androidMain` or `commonMain` source set:
```gradle
commonMain.dependencies {
    implementation("io.github.lilytreasure:multiplatformContacts:1.0.1")
}
```
## Usage


```kotlin
    var phoneNumber by remember { mutableStateOf("") }
    val multiplatformContactsPicker = pickMultiplatformContacts(onResult = {number->
        phoneNumber = number
    })

  Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                multiplatformContactsPicker.launch()
            }) {
                Text("Load contacts")
            }
            Text(text = phoneNumber)
        }

```
