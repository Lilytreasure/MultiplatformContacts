package di

import multiContacts.PlatformSpecific
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}, platform: PlatformSpecific?) = startKoin {
    appDeclaration()
    modules(
        module {
            factory {  platform }
        }
    )
}