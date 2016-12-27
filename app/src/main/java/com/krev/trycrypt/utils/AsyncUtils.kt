package com.krev.trycrypt.utils

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

    fun <T> async(block: suspend () -> T) = CompletableFuture<T>().apply {
        block.startCoroutine(object : Continuation<T> {
            override fun resume(value: T) {
                complete(value)
            }

            override fun resumeWithException(exception: Throwable) {
//                exception.printStackTrace()
                Log.e("AsyncUtils", exception.stackTrace.toString())
                throw exception
//                completeExceptionally(exception)
            }
        })
    }

    fun <T> asyncThread(block: suspend () -> T) = CompletableFuture<T>().apply {
        Thread {
            block.startCoroutine(object : Continuation<T> {
                override fun resume(value: T) {
                    complete(value)
                }

                override fun resumeWithException(exception: Throwable) {
                    Log.d("AsyncUtils", "Exception: ${exception.message}")
                    completeExceptionally(exception)
                }
            })
        }.start()
    }
}