package es.niux.efc.core.util.optional

import es.niux.efc.core.util.either.Either
import es.niux.efc.core.util.either.Left
import es.niux.efc.core.util.either.Right
import es.niux.efc.core.util.either.leftOr

typealias Optional<T> = Either<T, Unit>

@Suppress("FunctionName")
fun <T> Optional(value: T) = Left(value)

@Suppress("FunctionName")
fun Optional() = Right(Unit)

val Optional<*>.isEmpty get() = this.isRight

inline fun <T> Optional<T>.ifNotEmpty(
    action: (T) -> Unit
) = when (this) {
    is Either.Left -> action(this.value)
    is Either.Right -> Unit
}

inline fun Optional<*>.ifEmpty(
    action: () -> Unit
) = when (this) {
    is Either.Left -> Unit
    is Either.Right -> action()
}

inline fun <T, L : T> Optional<L>.getOr(
    ifEmpty: () -> T
) = this.leftOr { ifEmpty() }

fun <T> Optional<T>.getOrNull() = this.getOr { null }
