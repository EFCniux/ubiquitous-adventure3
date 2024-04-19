package es.niux.efc.data.source.network.demo.api

import es.niux.efc.data.source.network.demo.api.io.output.ItemOutput
import retrofit2.http.GET

interface DemoNetworkApi {
    @GET("mondly_android_code_task_json")
    suspend fun retrieveItems() : Set<ItemOutput>
}
