package multicontactSample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import multiContacts.pickMultiplatformContacts

@Composable
fun Sample() {
    var number by remember { mutableStateOf("") }
    val multiplatformContactsPicker = pickMultiplatformContacts(
        countryISOCode = "KE",
        onResult = {
            number = it
        })
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sample tests", modifier = Modifier.padding(bottom = 20.dp))
            Text(text = number)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(modifier = Modifier.padding(top = 16.dp),
                    onClick = {
                        multiplatformContactsPicker.launch()
                    }) {
                    Text("Load Contacts")
                }

            }
        }
    }
}