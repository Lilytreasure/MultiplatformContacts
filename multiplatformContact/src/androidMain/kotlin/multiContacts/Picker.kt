package multiContacts

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual  fun pickLoaded(){
    val context = LocalContext.current
    val singlePhoneNumberPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact(),
        onResult = { contactUri ->
            contactUri?.let { uri ->
                // Resolve the contact URI to get the phone number
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
    Button(onClick = { singlePhoneNumberPickerLauncher.launch() }) {
        Text("Pick Contact")
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
fun getPhoneNumberFromUri(context: Context, contactUri: Uri): String? {
    val cursor = context.contentResolver.query(contactUri, null, null, null, null)
    cursor?.use { c ->
        if (c.moveToFirst()) {
            // Get the column index for the phone number
            val phoneNumberIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            if (phoneNumberIndex != -1) {
                // Check if the column exists in the Cursor
                return c.getString(phoneNumberIndex)
            } else {
                // Log an error if the column index is not found
                Log.e("getPhoneNumberFromUri", "Phone number index not found in Cursor")
            }
        } else {
            // Log an error if the Cursor is empty
            Log.e("getPhoneNumberFromUri", "Cursor is empty")
        }
    }
    return null
}