package es.niux.efc.data.source.network

import es.niux.efc.core.exception.CoreException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException

@Throws(CoreException.Network::class)
suspend fun <T> tryNetworkRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    onError: suspend (
        e: CoreException.Network,
        response: Response?
    ) -> T = { e, _ -> throw e },
    doRequest: suspend () -> T
) = withContext(ioDispatcher) {
    try {
        doRequest()
    } catch (e: HttpException) {
        val coreEx = CoreException
            .Network.Server(
                code = e.code(),
                message = e.message()
            )

        onError(coreEx, e.response()?.raw())
    } catch (e: IOException) {
        val coreEx = CoreException
            .Network.Connection(
                cause = e
            )

        onError(coreEx, null)
    }
}
