

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.lilytreasure/multiplatformContacts"><img alt="Profile" src="https://badgen.net/badge/Maven Central/v1.0.0/blue?icon=github"/></a>
</p><br>

<p align="center">
👻 Multiplatform Contacts is a straight forward library used to get  Contacts in Android and iOS.
</p><br>


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
