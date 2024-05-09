package multiContacts

import kotlinx.coroutines.flow.MutableStateFlow


expect open class PlatformSpecific {

    val resultFromContact: MutableStateFlow<Map<String, String>>
    fun getContact(contactRequestCode: Int, alpha2Code: String): MutableStateFlow<Map<String, String>>
}

