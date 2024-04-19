package es.niux.efc.core.di.coroutines.dispatchers

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class CoroutinesDispatchersModule {
    @Provides
    @MainDispatcher
    fun providesMainDispatcher() = Dispatchers.Main.immediate

    @Provides
    @DefDispatcher
    fun providesDefaultDispatcher() = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun providesIoDispatcher() = Dispatchers.IO
}
