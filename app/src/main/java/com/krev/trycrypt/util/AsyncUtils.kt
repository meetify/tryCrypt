package com.krev.trycrypt.util

import android.util.Log
import java8.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.startCoroutine
import kotlin.coroutines.suspendCoroutine

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
object AsyncUtils {
    suspend fun <T> await(f: CompletableFuture<T>): T = suspendCoroutine {
        f.whenComplete { result, exception ->
            exception?.apply { it.resume(result) } ?: it.resumeWithException(exception)
        }
    }

    fun <T> async(block: suspend () -> T, givenCount: Int = 0): CompletableFuture<T>
            = CompletableFuture<T>().apply {
        var count = givenCount
        block.startCoroutine(object : Continuation<T> {
            override fun resume(value: T) {
                complete(value)
            }

            override fun resumeWithException(exception: Throwable) {
                Log.e("AsyncUtils", "Exception: ${exception.message}")
                exception.printStackTrace()
                completeExceptionally(exception)
                count++
                if (count > 1) return
                async(block, count)
            }
        })
    }

    fun <T> asyncThread(block: suspend () -> T) = CompletableFuture<T>().apply {
        Thread {
            async(block).thenApplyAsync {
                complete(it)
            }
        }.start()
    }
}