package net.onedaybeard.kotux

interface Reducer<S> {
	fun reduce(state: S, action: Action) : S
}

fun <S> Reducer(handler: (S, Action) -> S): Reducer<S>
	= object : Reducer<S> {
		override fun reduce(state: S, action: Action): S {
			return handler.invoke(state, action)
		}
	}