package es.niux.efc.data.parser

import es.niux.efc.core.exception.CoreException

@Throws(CoreException.Network.Parse::class)
suspend fun <T> tryParse(
    onError: suspend (
        e: CoreException.Network.Parse
    ) -> T = { e -> throw e },
    doParse: suspend () -> T
) = try {
    doParse()
} catch (e: Throwable) {
    val coreEx = CoreException
        .Network.Parse(cause = e)

    onError(coreEx)
}
