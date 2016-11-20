package net.onedaybeard.kotux;

interface Action

interface Store<S> {
	val state: S
	val reducer: Reducer<S>
	fun dispatch(action: Action)
	fun subscribe(subscriber: Subscriber<S>): Subscriber<S>
	fun subscribe(function: (S) -> Unit): Subscriber<S>
}

interface Intermediator<S> {
	val middlewares: List<Middleware<S>>
	fun middlewareByName(name: String) : Middleware<S>
}

interface Dispatcher<S> {
	fun dispatch(store: Store<S>, action: Action): Action?
}

interface Subscriptioner<S> {
	fun subscribe(function: (S) -> Unit): Subscriber<S>
	fun subscribe(subscriber: Subscriber<S>): Subscriber<S>
	fun updateSubscribers(state: S): Unit
	fun remove(subscriber: Subscriber<S>): Boolean
}

@Target(AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MiddlewareTag(val name : String)
