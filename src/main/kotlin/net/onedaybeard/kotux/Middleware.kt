package net.onedaybeard.kotux

interface Middleware<S> {
	fun dispatch(store: Store<S>, action: Action) : Action?
}

fun <S> Middleware(handler: (Store<S>, Action) -> Action?): Middleware<S>
	= object : Middleware<S> {
		override fun dispatch(store: Store<S>, action: Action): Action? {
			return handler.invoke(store, action)
		}
	}