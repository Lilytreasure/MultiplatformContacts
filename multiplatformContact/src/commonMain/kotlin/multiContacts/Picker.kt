package multiContacts

import androidx.compose.runtime.Composable

@Composable
expect fun pickMultiplatformContacts(
    countryISOCode: String,
    onResult: (String) -> Unit
): Launcher