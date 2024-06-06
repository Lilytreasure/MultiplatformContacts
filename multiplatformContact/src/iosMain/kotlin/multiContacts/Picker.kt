package multiContacts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Contacts.CNContact
import platform.Contacts.CNContactFormatter
import platform.Contacts.CNContactFormatterStyle
import platform.ContactsUI.CNContactPickerDelegateProtocol
import platform.ContactsUI.CNContactPickerViewController
import platform.UIKit.UIApplication
import platform.darwin.NSObject

typealias ContactPickedCallback = (String) -> Unit
@Composable
actual fun pickMultiplatformContacts(onResult: ContactPickedCallback): Launcher {
    val launcherCustom = remember {
        Launcher(onLaunch = {
            val picker = CNContactPickerViewController()
            picker.delegate = object : NSObject(), CNContactPickerDelegateProtocol {
                override fun contactPicker(picker: CNContactPickerViewController, didSelectContact: CNContact) {
                    val fullName = CNContactFormatter.stringFromContact(didSelectContact, CNContactFormatterStyle.CNContactFormatterStyleFullName)
                    onResult(fullName ?: "No Name")
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

