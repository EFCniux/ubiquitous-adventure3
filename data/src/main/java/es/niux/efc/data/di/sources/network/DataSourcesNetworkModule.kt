package es.niux.efc.data.di.sources.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.niux.efc.data.sourrces.network.demo.api.DemoNetworkApi
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesNetworkModule {
    @Provides
    @Singleton
    @Named("json")
    fun provideJsonStringFormat(): StringFormat = Json

    @Provides
    @Singleton
    @Named("json")
    fun provideJsonConverterFactory(
        @Named("json") json: StringFormat
    ) = json.asConverterFactory(
        MediaType.get("application/json; charset=UTF8")
    )

    @Provides
    @Singleton
    fun provideDemoNetworkApi(
        @Named("json") jsonConverterFactory: Converter.Factory
    ): DemoNetworkApi = Retrofit.Builder()
        .baseUrl("https://europe-west1-mondly-workflows.cloudfunctions.net")
        .addConverterFactory(jsonConverterFactory)
        .build()
        .create(DemoNetworkApi::class.java)
}
