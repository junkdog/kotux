## kotux

A minimalist and modular redux-like predictive state container for kotlin

(note to self: at least put something basic here... to anyone reading - sorry)

## Gettings started

#### Initialize the store

```kotlin
val store = Kotux(
	initial = MyApplicationState(),
	reducer = myReducer,
	middleware = listOf(
		logger, download, persistence, appState))
```

## Design
### Middleware
### Actions
### Reducers
### Middleware
