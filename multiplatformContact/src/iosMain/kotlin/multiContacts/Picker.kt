package multiContacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun pickLoaded(): Launcher {
    var launcherCustom: Launcher? = null

    launcherCustom = remember {
        Launcher(onLaunch = {

        })
    }

    return launcherCustom
}