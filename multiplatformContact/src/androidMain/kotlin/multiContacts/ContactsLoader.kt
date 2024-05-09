package multiContacts

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import kotlinx.coroutines.flow.MutableStateFlow


actual open class PlatformSpecific(private val context: Context) {
    private val phoneUtil = PhoneNumberUtil.getInstance()
    private val currentActivity: AppCompatActivity = (context as AppCompatActivity)
    private val contactRequestCode = 421
    private var contactRequestCodeValue: Int? = null
    private var alpha2CodeValue: String? = null
    private fun handleOnActivityResult(
        activity: Activity?,
        requestCode: Int?,
        country: String?,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == contactRequestCode) {
            if (resultCode == Activity.RESULT_OK && data !== null) {
                var cursor: Cursor? = null

                try {
                    val contactUri: Uri? = data.data

                    val projection = arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    )

                    if (contactUri !== null) {
                        cursor = activity!!.contentResolver
                            .query(contactUri, projection, null, null, null)

                        var number: String? = ""

                        var name: String? = ""

                        if (cursor !== null && cursor.count > 0) {
                            if (cursor.moveToFirst()) {
                                val column =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                number = cursor.getString(column)
                                val column0 =
                                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                                name = cursor.getString(column0)
                            }
                        }

                        val phoneNumber: Phonenumber.PhoneNumber =
                            phoneUtil.parseAndKeepRawInput(number, country)

                        if (name !== null) {
                            resultFromContact.value =
                                mapOf(name to "${phoneNumber.countryCode}${phoneNumber.nationalNumber}")
                        }

                    }


                } catch (e: Exception) {
                    resultFromContact.value =
                        mapOf("CONTACT_PICKER_FAILED" to e.stackTraceToString())
                } finally {
                    if (cursor !== null) {
                        cursor.close()
                    }
                }
            }
        }
    }

    private val startForResult =
        currentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data

                if (intent != null) {
                    println(intent.extras)

                    handleOnActivityResult(
                        currentActivity,
                        contactRequestCodeValue,
                        alpha2CodeValue,
                        result.resultCode,
                        intent
                    )
                }
            }
        }

    actual fun getContact(
        contactRequestCode: Int,
        alpha2Code: String
    ): MutableStateFlow<Map<String, String>> {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                currentActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                100
            );
            resultFromContact.value = mapOf("E_SHOW_CONTACTS_PERMISSION" to "TRUE")
            return resultFromContact
        } else {
            return try {
                alpha2CodeValue = alpha2Code
                contactRequestCodeValue = contactRequestCode
                val contacts = Intent(Intent.ACTION_PICK).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                        setDataAndType(
                            ContactsContract.Contacts.CONTENT_URI,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                        )
                    }
                }
                startForResult.apply {
                    launch(contacts)
                }
                resultFromContact.value = mapOf("ACTIVITY_STARTED" to "TRUE")
                resultFromContact
            } catch (e: Exception) {
                e.printStackTrace()
                resultFromContact.value = mapOf("E_FAILED_TO_SHOW_PICKER" to e.stackTraceToString())
                resultFromContact
            }
        }
    }

    actual val resultFromContact: MutableStateFlow<Map<String, String>> =
        MutableStateFlow(emptyMap())

}

