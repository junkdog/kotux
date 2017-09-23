package net.onedaybeard.kotux

class Reducer<S>(private var handler: (S, Action) -> S) {
    fun reduce(state: S, action: Action) = handler(state, action)
}

//fun <S> Reducer(handler: (S, Action) -> S): = object : Reducer<S> {
//    override fun reduce(state: S, action: Action): S {
//        return handler.invoke(state, action)
//    }
//}