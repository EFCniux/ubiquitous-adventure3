package es.niux.efc.core.util.either

sealed class Either<out L, out R> {
    data class Left<T>(
        val value: T
    ) : Either<T, Nothing>()

    data class Right<T>(
        val value: T
    ) : Either<Nothing, T>()

    val isLeft get() = this is Left
    val isRight get() = this is Right

    inline fun ifLeft(
        action: (L) -> Unit
    ) = when (this) {
        is Left -> action(this.value)
        is Right -> Unit
    }

    inline fun ifRight(
        action: (R) -> Unit
    ) = when (this) {
        is Left -> Unit
        is Right -> action(this.value)
    }

    inline fun <T> fold(
        ifLeft: (L) -> T,
        ifRight: (R) -> T
    ) = when (this) {
        is Left -> ifLeft(this.value)
        is Right -> ifRight(this.value)
    }

    fun leftOrNull() = leftOr { null }
    fun rightOrNull() = rightOr { null }
}

fun <T> Left(value: T) = Either.Left(value)
fun <T> Right(value: T) = Either.Right(value)

inline fun <T, L : T, R> Either<L, R>.leftOr(
    ifRight: (R) -> T
) = when (this) {
    is Either.Left -> this.value
    is Either.Right -> ifRight(this.value)
}

inline fun <T, L, R : T> Either<L, R>.rightOr(
    ifLeft: (L) -> T
) = when (this) {
    is Either.Left -> ifLeft(this.value)
    is Either.Right -> this.value
}
