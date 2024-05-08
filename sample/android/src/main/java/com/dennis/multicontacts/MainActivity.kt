
package com.dennis.multicontacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dennis.multicontacts.ui.theme.ComposeSignatureTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSignatureTheme(darkTheme = false) {

            }
        }
    }
}
