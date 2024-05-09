package multiContacts

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class Loader(): Multi , KoinComponent {
    override val platformSpecific: PlatformSpecific by inject<PlatformSpecific>()
}