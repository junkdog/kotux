package net.onedaybeard.kotux

class Middleware<S>(private var handler: (Store<S>, Action) -> Action?) {
    fun dispatch(store: Store<S>, action: Action) = handler(store, action)
}