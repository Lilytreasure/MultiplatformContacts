package multiContacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun pickMultiplatformContacts(onResult: (String) -> Unit): Launcher {
    val launcherCustom: Launcher?
    //Todo--Ios contacts Picker Implementation

    launcherCustom = remember {
        Launcher(onLaunch = {

        })
    }

    return launcherCustom
}