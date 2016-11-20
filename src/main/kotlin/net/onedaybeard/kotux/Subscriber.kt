package net.onedaybeard.kotux

interface Subscriber<S> {
	fun onStateChange(state: S) : Unit
}

fun <S> Subscriber(handler: (S) -> Unit) : Subscriber<S>
	= object : Subscriber<S> {
		override fun onStateChange(state: S) {
			handler.invoke(state)
		}
	}