package multiContacts

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager

@Composable
actual  fun pickLoaded(){
    val context = LocalContext.current
    val currentActivity: AppCompatActivity = (context as AppCompatActivity)
    val singlePhoneNumberPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = { contactUri ->
            contactUri?.let { uri ->
                // Resolve the contact URI to get the phone number
                println("Result from picked contacts;;;;;;" + uri.query.toString())

                val phoneNumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getPhoneNumberFromUri(context, uri)
                } else {
                    TODO("VERSION.SDK_INT < JELLY_BEAN")
                }
                // Handle the retrieved phone number
                if (!phoneNumber.isNullOrEmpty()) {

                    println("Loaded phone Number;;;;" + phoneNumber)

                }
            }
        }
    )
    Button(onClick = {
        if (ContextCompat.checkSelfPermission(
                currentActivity,
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    Manifest.permission.READ_CONTACTS
                } else {
                    Manifest.permission.READ_CONTACTS
                }
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            singlePhoneNumberPickerLauncher.launch()

        } else {
            ActivityCompat.requestPermissions(
                currentActivity,
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                    arrayOf(Manifest.permission.READ_CONTACTS)
                } else {
                    arrayOf(Manifest.permission.READ_CONTACTS)
                },
                33
            )
        }

    }) {
        Text("Pick Contact")
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
fun getPhoneNumberFromUri(context: Context, contactUri: Uri): String? {
    val cursor = context.contentResolver.query(contactUri, null, null, null, null)
    cursor?.use { c ->
        if (c.moveToFirst()) {
            // Get the column index for the phone number
            val phoneNumberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            if (phoneNumberIndex != -1) {
                // Check if the column exists in the Cursor
                val phoneNumber = c.getString(phoneNumberIndex)
                if (!phoneNumber.isNullOrBlank()) {
                    return phoneNumber
                } else {
                    Log.e("getPhoneNumberFromUri", "Phone number is null or blank")
                }
            } else {
                // Log an error if the column index is not found
                Log.e("getPhoneNumberFromUri", "Phone number index not found in Cursor: $contactUri")
            }
        } else {
            // Log an error if the Cursor is empty
            Log.e("getPhoneNumberFromUri", "Cursor is empty")
        }
    }
    return null
}
