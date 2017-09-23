package net.onedaybeard.kotux

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class KotuxDispatcherTest {
    class INIT : Action
    class FIRST : Action
    class SECOND : Action

    private fun reducer() = Reducer<AppState> { state, action ->
        when (action) {
            is INIT   -> AppState()
            is FIRST  -> state.copy(finishedFirst = true)
            is SECOND -> state.copy(finishedSecond = true)
            else      -> state
        }
    }

    val dispatachAtomicCheck = Middleware<AppState> { store, action ->
        when (action) {
            is FIRST  -> {
                assertFalse(store.state.finishedFirst)
                assertFalse(store.state.finishedSecond)
            }
            is SECOND -> {
                assertTrue(store.state.finishedFirst)
                assertFalse(store.state.finishedSecond)
            }
        }
        action
    }

    val firstDispatchesSecond = Middleware<AppState> { store, action ->
        if (action is FIRST)
            store.dispatch(SECOND())

        action
    }

    data class AppState(val finishedFirst: Boolean = false,
                        val finishedSecond: Boolean = false)

    @Test
    fun simple_dispatch() {
        val store = Kotux(AppState(), reducer(), listOf(dispatachAtomicCheck))
        store.dispatch(INIT())
        store.dispatch(FIRST())
        store.dispatch(SECOND())

        assertTrue(store.state.finishedFirst)
        assertTrue(store.state.finishedSecond)
    }

    @Test
    fun sequential_dispatch() {
        val store = Kotux(AppState(), reducer(),
                          listOf(firstDispatchesSecond, dispatachAtomicCheck))
        store.dispatch(INIT())
        store.dispatch(FIRST())

        assertTrue(store.state.finishedFirst)
        assertTrue(store.state.finishedSecond)
    }

    @Test(expected = AssertionError::class)
    fun sequential_dispatch_fail1() {
        val store = Kotux(AppState(), reducer(), listOf(dispatachAtomicCheck))
        store.dispatch(INIT())
        store.dispatch(FIRST())
        store.dispatch(FIRST())
    }

    @Test(expected = AssertionError::class)
    fun sequential_dispatch_fail2() {
        val store = Kotux(AppState(), reducer(), listOf(dispatachAtomicCheck))
        store.dispatch(INIT())
        store.dispatch(SECOND())
    }
}
