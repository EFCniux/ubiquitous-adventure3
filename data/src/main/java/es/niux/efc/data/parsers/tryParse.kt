package es.niux.efc.data.parsers

import es.niux.efc.core.exception.CoreException
import kotlinx.coroutines.CancellationException

@Throws(CoreException.Network.Parse::class)
suspend fun <T> tryParse(
    onError: suspend (
        e: CoreException.Network.Parse
    ) -> T = { e -> throw e },
    doParse: suspend () -> T
) = try {
    doParse()
} catch (e: Exception) {
    if (e is CancellationException)
        throw e

    val coreEx = CoreException
        .Network.Parse(cause = e)

    onError(coreEx)
}
