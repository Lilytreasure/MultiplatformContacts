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
import android.database.Cursor

@Composable
actual fun pickLoaded() {
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
            val phoneNumberIndex =
                c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
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
                Log.e(
                    "getPhoneNumberFromUri",
                    "Phone number index not found in Cursor: $contactUri"
                )
            }
        } else {
            // Log an error if the Cursor is empty
            Log.e("getPhoneNumberFromUri", "Cursor is empty")
        }
    }
    return null
}

fun getContacts(context: Context): List<Pair<String, String>> {
    val contactsList = mutableListOf<Pair<String, String>>()

    // Define the columns you want to retrieve
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    // Query the contacts database
    val cursor: Cursor? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
    } else {
        TODO("VERSION.SDK_INT < ECLAIR")
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        cursor?.use {
            // Get the indexes of the columns
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneNumberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            // Check if the indexes are valid
            if (nameIndex != -1 && phoneNumberIndex != -1) {
                // Iterate over the cursor to retrieve the data
                while (it.moveToNext()) {
                    val name = it.getString(nameIndex)
                    val phoneNumber = it.getString(phoneNumberIndex)

                    if (!phoneNumber.isNullOrBlank()) {
                        contactsList.add(Pair(name, phoneNumber))
                    } else {
                        Log.e("getContacts", "Phone number is null or blank for contact: $name")
                    }
                }
            } else {
                if (nameIndex == -1) Log.e("getContacts", "Name index not found in Cursor")
                if (phoneNumberIndex == -1) Log.e(
                    "getContacts",
                    "Phone number index not found in Cursor"
                )
            }
        } ?: run {
            Log.e("getContacts", "Cursor is null")
        }
    }

    return contactsList
}
