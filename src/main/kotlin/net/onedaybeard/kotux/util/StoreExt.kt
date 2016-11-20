package net.onedaybeard.kotux.util

import net.onedaybeard.kotux.*
import kotlin.reflect.KProperty1


fun <S> Store<S>.subscribe(property: KProperty1<S, Any>,
                           subscriber: (S) -> Unit) : Subscriber<S> {

	val check : StateObserver<S> = SliceObserver(property)
	return this.subscribe { state ->
		if (check.isStateChanged(state))
			subscriber.invoke(state)
	}
}

fun <S> Store<S>.subscribe(check: (S) -> Boolean,
                           subscriber: (S) -> Unit) : Subscriber<S> {

	return this.subscribe { state ->
		if (check.invoke(state)) {
			subscriber.invoke(state)
		}
	}
}
