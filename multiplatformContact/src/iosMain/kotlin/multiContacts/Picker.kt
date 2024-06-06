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

@Composable
actual fun pickMultiplatformContacts(onResult: (String) -> Unit): Launcher {
    val launcherCustom: Launcher?
    //Todo--Ios contacts Picker Implementation
    val picker = CNContactPickerViewController()
    picker.delegate = object : NSObject(), CNContactPickerDelegateProtocol {
        override fun contactPicker(picker: CNContactPickerViewController, didSelectContact: CNContact) {
            val fullName = CNContactFormatter.stringFromContact(didSelectContact, CNContactFormatterStyle.CNContactFormatterStyleFullName)
            //onContactPicked(fullName ?: "No Name")
        }
    }
    launcherCustom = remember {
        Launcher(onLaunch = {
            UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                picker,
                true,
                null,
            )

        })
    }

    return launcherCustom
}

