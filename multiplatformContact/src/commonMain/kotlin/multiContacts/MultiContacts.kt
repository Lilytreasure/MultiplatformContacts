package multiContacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultiContacts(
    modifier: Modifier = Modifier,
) {

 pickLoaded()

    Column(){
        Button(modifier = Modifier.padding(top = 16.dp),
            onClick = {

        }){
           Text("Load")
        }


    }



}

