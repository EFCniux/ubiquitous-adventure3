package es.niux.efc.core.util.optional

import es.niux.efc.core.util.either.Either
import es.niux.efc.core.util.either.Left
import es.niux.efc.core.util.either.Right
import es.niux.efc.core.util.either.leftOr

typealias Optional<T> = Either<T, Unit>

fun <T> Optional(value: T) = Left(value)

fun Optional() = Right(Unit)

val Optional<*>.isEmpty get() = this.isRight

fun <T, L : T> Optional<L>.get(
    ifEmpty: () -> T
) = this.leftOr { ifEmpty() }

fun <T> Optional<T>.getOrNull() = this.get { null }
