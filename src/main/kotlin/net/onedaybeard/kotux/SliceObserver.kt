package net.onedaybeard.kotux

import kotlin.reflect.KProperty1

interface StateObserver<in S> {
	fun isStateChanged(state: S): Boolean
}

class SliceObserver<S>(val property: KProperty1<S, Any>) : StateObserver<S>  {

	private var observedSlice: Any = Unit

	override fun isStateChanged(state: S): Boolean {
		var slice = property.get(state)
		if (observedSlice !== slice) {
			observedSlice = slice
			return true
		} else {
			return false
		}
	}
}
