
package com.dennis.multicontacts

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.dennis.multicontacts.ui.theme.ComposeSignatureTheme
import multicontactSample.Sample

class MainActivity : AppCompatActivity() {
    private var currentActivity: Activity? = null
    private fun setCurrentActivity(activity: Activity) {
        currentActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCurrentActivity(this)
        setContent {
            ComposeSignatureTheme(darkTheme = false) {
                Sample()
            }
        }
    }
    override fun onResume() {
        setCurrentActivity(this)
        super.onResume()

    }
}
