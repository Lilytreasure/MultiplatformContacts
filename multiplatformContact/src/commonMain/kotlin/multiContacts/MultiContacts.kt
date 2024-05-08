package multiContacts

import TimerManager
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
public fun MultiContacts(
    modifier: Modifier = Modifier,
){
    val timerManager = remember { TimerManager() }
    Text("Contacts")

}