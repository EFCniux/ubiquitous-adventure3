package es.niux.efc.data.di.sources.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.niux.efc.data.sourrces.network.demo.api.DemoNetworkApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class DataSourcesNetworkModule {
    @Provides
    fun provideDemoNetworkApi(): DemoNetworkApi = Retrofit.Builder()
        .baseUrl("https://europe-west1-mondly-workflows.cloudfunctions.net")
        .build()
        .create(DemoNetworkApi::class.java)
}
