package multiContacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
@Composable
actual fun pickLoaded() {
    val context = LocalContext.current
    val currentActivity: AppCompatActivity = (context as AppCompatActivity)
    val result = remember { mutableStateOf<Bitmap?>(null) }
    val resultContacts = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        result.value = it
    }
    val launcherContacts =
        rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) {
            resultContacts.value = it
        }
    var phoneNumber by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(resultContacts.value) {
        resultContacts.value?.let { uri ->
            phoneNumber = getPhoneNumberFromUriData(context, uri)
        }
    }
    Column() {
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
                launcherContacts.launch()
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
            Text(text = "Pick Number")
        }
        result.value?.let { image ->
            Image(image.asImageBitmap(), null, modifier = Modifier.fillMaxWidth())
        }
        resultContacts.value?.let { text ->
            phoneNumber?.let { Text(text = it) }
        }
    }
}

fun getPhoneNumberFromUriData(context: Context, uri: Uri): String? {
    val contentResolver = context.contentResolver
    val cursor: Cursor? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(uri.lastPathSegment),
            null
        )
    } else {
        TODO("VERSION.SDK_INT < ECLAIR")
    }

    var phoneNumber: String? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        cursor?.use {
            if (it.moveToFirst()) {
                phoneNumber = it.getString(
                    it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
            }
        }
    }

    return phoneNumber
}
