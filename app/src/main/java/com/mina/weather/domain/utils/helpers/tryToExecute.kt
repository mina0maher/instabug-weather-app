package com.mina.weather.domain.utils.helpers

import java.io.IOException
import java.net.SocketTimeoutException
import com.mina.weather.domain.utils.states.Result

inline fun <T> tryToExecute(action: () -> T): Result<T> {
    return try {
        Result.Success(action())
    } catch (e: IllegalArgumentException) {
        Result.Error("Invalid request, please try again")
    } catch (e: SocketTimeoutException) {
        Result.Error("Request timed out, please try again")
    } catch (e: IOException) {
        Result.Error("Network error, please try again")
    } catch (e: Exception) {
        Result.Error("Unexpected error, please try again")
    }
}