package es.niux.efc.core.exception

sealed class CoreException : RuntimeException() {
    sealed class Network : CoreException() {
        class Connection(
            override val message: String? = null,
            override val cause: Throwable? = null
        ) : Network()

        class Server(
            val code: Int,
            override val message: String
        ) : Network()

        class Parse(
            override val message: String? = null,
            override val cause: Throwable? = null
        ) : Network()
    }
}