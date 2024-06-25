package es.niux.efc.data.di.sources.local.room

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.niux.efc.data.sourrces.local.room.demo.database.DEMO_DB_NAME
import es.niux.efc.data.sourrces.local.room.demo.database.DemoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesLocalRoomModule {
    @Provides
    @Singleton
    fun provideDemoDatabase(
        @ApplicationContext applicationContext: Context
    ): DemoDatabase = Room
        .databaseBuilder(
            context = applicationContext,
            klass = DemoDatabase::class.java,
            name = DEMO_DB_NAME
        )
        //.enableMultiInstanceInvalidation()
        .build()
}
