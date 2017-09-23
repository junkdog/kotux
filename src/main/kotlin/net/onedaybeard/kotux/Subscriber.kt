package net.onedaybeard.kotux

class Subscriber<S>(private var onStateChanger: (S) -> Unit) {
    fun onStateChange(state: S) = onStateChanger(state)
    fun unsubscribe(store: Kotux<S>) = store.remove(this)
}

//fun <S> Subscriber(handler: (S) -> Unit): Subscriber<S>
//    = object : Subscriber<S> {
//    override fun onStateChange(state: S) {
//        handler.invoke(state)
//    }
//}

