package multiContacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Contacts.CNContact
import platform.ContactsUI.CNContactPickerDelegateProtocol
import platform.ContactsUI.CNContactPickerViewController
import platform.UIKit.UIApplication
import platform.darwin.NSObject


/**
 * @param Launcher  used to invoke the contacts picker
 * @param extractPhoneNumber is used to extract phone number, can be modified to extract phone number data of your choice
 */

typealias ContactPickedCallback = (String) -> Unit


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun pickMultiplatformContacts(
    countryISOCode: String,
    onResult: ContactPickedCallback
): Launcher {
//    val phoneUtil = NBPhoneNumberUtil()
//    try {
//        val parsedNumber: NBPhoneNumber? = phoneUtil.parse("0003455", "KE",null)
//        // Check if parsedNumber is null before formatting
//        if (parsedNumber != null) {
//            val formattedString: String? = phoneUtil.format(parsedNumber, NBEPhoneNumberFormatE164,null)
//            println("Formatted phone number: $formattedString")
//        } else {
//            println("Parsed number is null.")
//        }
//
//    } catch (e: Exception) {
//        println("Exception occurred: $e")
//    }

    val launcherCustom = remember {
        Launcher(onLaunch = {
            val picker = CNContactPickerViewController()
            picker.delegate = object : NSObject(), CNContactPickerDelegateProtocol {
                override fun contactPicker(picker: CNContactPickerViewController, didSelectContact: CNContact) {
                    val phoneNumber = didSelectContact.phoneNumbers.firstOrNull()?.toString()
                    val phoneNumberExTracted = phoneNumber?.let { extractPhoneNumber(it) }
                    onResult(phoneNumberExTracted ?: "No Phone Number")
                }
                override fun contactPickerDidCancel(picker: CNContactPickerViewController) {
                    onResult("")
                }
            }
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                picker,
                true,
                null,
            )
        })
    }
    return launcherCustom
}
fun extractPhoneNumber(cnlabeledValue: String): String? {
    val regex = Regex("stringValue=([^,]+)")
    val matchResult = regex.find(cnlabeledValue)
    return matchResult?.groupValues?.get(1)
}

