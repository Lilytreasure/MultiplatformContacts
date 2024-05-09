package multiContacts

import TimerManager
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultiContacts(
    modifier: Modifier = Modifier,
) {
    val load = Loader()
    val timerManager = remember { TimerManager() }

    var shownumber by remember { mutableStateOf("") }
    Button(modifier = Modifier.padding(top = 16.dp),
        onClick = {
            load.platformSpecific.getContact(421, "2")
        }) {
        Text(text = "Loaded")
    }
    Text(shownumber)

}