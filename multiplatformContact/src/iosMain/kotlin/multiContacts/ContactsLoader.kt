package multiContacts

import kotlinx.coroutines.flow.MutableStateFlow


actual open class PlatformSpecific {
    actual val resultFromContact: MutableStateFlow<Map<String, String>>
        get() = TODO("Not yet implemented")
    actual fun getContact(
        contactRequestCode: Int,
        alpha2Code: String
    ): MutableStateFlow<Map<String, String>> {
        TODO("Not yet implemented")
    }
}
