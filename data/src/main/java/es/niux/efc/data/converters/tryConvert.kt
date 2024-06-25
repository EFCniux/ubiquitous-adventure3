package es.niux.efc.data.converters

import es.niux.efc.core.exception.CoreException
import kotlinx.coroutines.CancellationException

@Throws(Exception::class)
private suspend fun <T> tryConvert(
    onError: suspend (
        e: Exception
    ) -> T = { e -> throw e },
    doParse: suspend () -> T
) = try {
    doParse()
} catch (e: Exception) {
    if (e is CancellationException)
        throw e

    onError(e)
}

@Throws(CoreException.Network.Parse::class)
suspend fun <T> tryConvertNetwork(
    onError: suspend (
        e: CoreException.Network.Parse
    ) -> T = { e -> throw e },
    doParse: suspend () -> T
) = tryConvert(
    doParse = doParse,
    onError = {
        val coreEx = CoreException
            .Network.Parse(
                message = "Unable to convert raw data",
                cause = it
            )

        onError(coreEx)
    }
)

@Throws(CoreException.Local.Database::class)
suspend fun <T> tryConvertLocalDatabase(
    onError: suspend (
        e: CoreException.Local.Database
    ) -> T = { e -> throw e },
    doParse: suspend () -> T
) = tryConvert(
    doParse = doParse,
    onError = {
        val coreEx = CoreException
            .Local.Database(
                message = "Unable to convert raw data",
                cause = it
            )

        onError(coreEx)
    }
)
