package net.onedaybeard.kotux.util

import java.util.concurrent.locks.ReentrantLock

inline fun <T> ReentrantLock.tryLock(action: () -> T, actionIfLocked: () -> T): T {
    if (!isHeldByCurrentThread && tryLock()) {
        try {
            return action()
        } finally {
            unlock()
        }
    } else {
        return actionIfLocked()
    }
}