package multiContacts

import TimerManager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
public fun MultiContacts(
    modifier: Modifier = Modifier,
) {
    val timerManager = remember { TimerManager() }
    var shownumber by remember { mutableStateOf("") }
    contactsLoader { loaded ->
        if (loaded != null) {
            shownumber = loaded
        }
    }
    Text(shownumber)

}