package es.niux.efc.domain.interactors

import app.cash.turbine.test
import es.niux.efc.core.entity.Item
import es.niux.efc.core.exception.CoreException
import es.niux.efc.core.util.optional.Optional
import es.niux.efc.data.repositories.DemoRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MainInteractorUnitTest {
    private lateinit var repository: DemoRepository
    private lateinit var item: Item
    private lateinit var error: CoreException

    @Before
    fun setup() {
        repository = mockk<DemoRepository>()
        item = mockk<Item>()
        error = mockk<CoreException.Network.Connection>()
    }

    @Test
    fun `flow should load data`() = runTest {
        val interactor = MainInteractor(
            defDispatcher = StandardTestDispatcher(testScheduler),
            repository = repository
        )

        every { repository.observeItems() } returns flow {
            emit(Optional(setOf(item)))
        }

        interactor
            .invoke(reload = flow { while (true) yield() })
            .test {
                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = null,
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = listOf(item),
                        error = null
                    ), awaitItem()
                )
                expectNoEvents()
            }
    }

    @Test
    fun `flow should load error`() = runTest {
        val interactor = MainInteractor(
            defDispatcher = StandardTestDispatcher(testScheduler),
            repository = repository
        )

        every { repository.observeItems() } returns flow {
            throw error
        }

        interactor
            .invoke(reload = flow { while (true) yield() })
            .test {
                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = null,
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = null,
                        error = error
                    ), awaitItem()
                )
                expectNoEvents()
            }
    }

    @Test
    fun `flow should reload data`() = runTest {
        val interactor = MainInteractor(
            defDispatcher = StandardTestDispatcher(testScheduler),
            repository = repository
        )
        val reload = MutableSharedFlow<Unit>()

        every { repository.observeItems(forceRefresh = false) } returns flow {
            emit(Optional(setOf(item)))
        }

        every { repository.observeItems(forceRefresh = true) } returns flow {
            emit(Optional(setOf(item)))
        }

        interactor
            .invoke(reload = reload)
            .test {
                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = null,
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = listOf(item),
                        error = null
                    ), awaitItem()
                )
                expectNoEvents()

                reload.emit(Unit)

                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = listOf(item),
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = listOf(item),
                        error = null
                    ), awaitItem()
                )
                expectNoEvents()
            }
    }

    @Test
    fun `flow should reload error`() = runTest {
        val interactor = MainInteractor(
            defDispatcher = StandardTestDispatcher(testScheduler),
            repository = repository
        )
        val reload = MutableSharedFlow<Unit>()

        every { repository.observeItems(forceRefresh = false) } returns flow {
            throw error
        }

        every { repository.observeItems(forceRefresh = true) } returns flow {
            emit(Optional(setOf(item)))
        }

        interactor
            .invoke(reload = reload)
            .test {
                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = null,
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = null,
                        error = error
                    ), awaitItem()
                )
                expectNoEvents()

                reload.emit(Unit)

                assertEquals(
                    MainInteractor.State(
                        isLoading = true,
                        data = null,
                        error = null
                    ),
                    awaitItem()
                )
                assertEquals(
                    MainInteractor.State(
                        isLoading = false,
                        data = listOf(item),
                        error = null
                    ), awaitItem()
                )
                expectNoEvents()
            }
    }
}