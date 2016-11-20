package net.onedaybeard.kotux;

import net.onedaybeard.kotux.util.tryLock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Kotux<S>(initial: S,
               override val reducer: Reducer<S>,
               middleware: List<Middleware<S>>,
               dispatcher: Dispatcher<S> = StdDispatcher(middleware),
               intermediator: Intermediator<S> = StdIntermediator(middleware),
               notifier: Subscriptioner<S> = StdSubscriptioner()) :

		Store<S>,
		Dispatcher<S> by dispatcher,
		Intermediator<S> by intermediator,
		Subscriptioner<S> by notifier {


	override var state: S = initial
		private set

	private val actions = mutableListOf<Action>()
	private val nextActions = mutableListOf<Action>()

	val dispatchLock = ReentrantLock()
	val nextDispatchLock = ReentrantLock()

	override fun dispatch(action: Action) {
		nextDispatchLock.withLock { nextActions.add(action) }
		dispatchLock.tryLock({
			do {
				val hasMoreActions = dispatchBatch()
			} while (hasMoreActions)
		}, {})
	}

	private fun dispatchBatch() : Boolean {
		nextDispatchLock.withLock {
			actions.addAll(nextActions)
			nextActions.clear()
		}

		for (currentAction in actions) {
			dispatch(this, currentAction)?.let { action ->
				state = reducer.reduce(state, action)
			}
		}

		actions.clear()
		updateSubscribers(state)

		return nextDispatchLock.withLock { nextActions.isNotEmpty() }
	}
}


class StdSubscriptioner<S>(val subscribers: MutableList<Subscriber<S>> = mutableListOf()) :
	Subscriptioner<S> {

	override fun subscribe(subscriber: Subscriber<S>): Subscriber<S> {
		subscribers.add(subscriber)
		return subscriber
	}

	override fun subscribe(function: (S) -> Unit): Subscriber<S> {
		return subscribe(Subscriber(function))
	}

	override fun remove(subscriber: Subscriber<S>) : Boolean {
		return subscribers.remove(subscriber)
	}

	override fun updateSubscribers(state: S): Unit {
		subscribers.forEach { it.onStateChange(state) }
	}
}

class StdIntermediator<S>(override val middlewares: List<Middleware<S>>) : Intermediator<S> {
	override fun middlewareByName(name: String): Middleware<S> {
//		return middlewares.first{ it.name == name }
		return middlewares[0]
	}
}

class StdDispatcher<S>(val middlewares: List<Middleware<S>>) : Dispatcher<S> {
	override fun dispatch(store: Store<S>, action: Action): Action? {
		var a: Action = action
		for (mw in middlewares)
			a = mw.dispatch(store, a) ?: return null

		return a
	}
}
