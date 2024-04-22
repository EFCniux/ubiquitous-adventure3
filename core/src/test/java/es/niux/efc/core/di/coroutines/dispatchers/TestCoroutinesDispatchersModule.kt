package es.niux.efc.core.di.coroutines.dispatchers

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.test.StandardTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoroutinesDispatchersModule::class]
)

class TestCoroutinesDispatchersModule {
    @Provides
    @MainDispatcher
    fun providesMainDispatcher() = StandardTestDispatcher()

    @Provides
    @DefDispatcher
    fun providesDefaultDispatcher() = StandardTestDispatcher()

    @Provides
    @IoDispatcher
    fun providesIoDispatcher() = StandardTestDispatcher()
}