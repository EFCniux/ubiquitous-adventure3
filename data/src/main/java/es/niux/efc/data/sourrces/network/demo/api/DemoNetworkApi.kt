package es.niux.efc.data.sourrces.network.demo.api

import es.niux.efc.data.sourrces.network.demo.api.io.output.ItemOutputSet
import retrofit2.http.GET

interface DemoNetworkApi {
    @GET("mondly_android_code_task_json")
    suspend fun retrieveItems() : ItemOutputSet
}
