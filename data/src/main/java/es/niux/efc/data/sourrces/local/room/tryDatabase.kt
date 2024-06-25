package es.niux.efc.data.sourrces.local.room

import es.niux.efc.core.exception.CoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Throws(CoreException.Local.Database::class)
suspend fun <T> trySusDatabaseTask(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    onError: suspend (
        e: CoreException.Local.Database
    ) -> T = { e -> throw e },
    doTask: suspend () -> T
) = withContext(ioDispatcher) {
    try {
        doTask()
    } catch (e: Exception) {
        if (e is CancellationException)
            throw e
        else {
            val coreEx = CoreException
                .Local.Database(
                    cause = e
                )

            onError(coreEx)
        }
    }
}

@Throws(CoreException.Local.Database::class)
fun <T> tryDatabaseTask(
    onError: (
        e: CoreException.Local.Database
    ) -> T = { e -> throw e },
    doTask: () -> T
) = try {
    doTask()
} catch (e: Exception) {
    val coreEx = CoreException
        .Local.Database(
            cause = e
        )

    onError(coreEx)
}

@Throws(CoreException.Local.Database::class)
fun <T> Flow<T>.tryDatabaseFlow(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    onError: (
        e: CoreException.Local.Database
    ) -> T = { e -> throw e }
) = this
    .catch { e ->
        val coreEx = CoreException
            .Local.Database(
                cause = e
            )

        onError(coreEx)
    }
    .distinctUntilChanged()
    .flowOn(ioDispatcher)
