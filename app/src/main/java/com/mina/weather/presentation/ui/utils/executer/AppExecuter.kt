package com.mina.weather.presentation.ui.utils.executer

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutors {
    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    fun executeOnDiskIO(block: () -> Unit) {
        singleThreadExecutor.execute(block)
    }

    fun postToMainThread(block: () -> Unit) {
        mainThreadHandler.post(block)
    }
}