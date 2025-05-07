package com.mina.weather.domain.utils.helpers

import com.mina.weather.data.utils.NoCachedDataException
import com.mina.weather.data.utils.NoDataAvailableException
import java.io.IOException
import java.net.SocketTimeoutException
import com.mina.weather.domain.utils.states.Result

inline fun <T> tryToExecute(action: () -> T): Result<T> {
    return try {
        Result.Success(action())
    } catch (e: NoCachedDataException) {
        Result.Error("No cached data available, please connect to internet")
    } catch (e: NoDataAvailableException) {
        Result.Error("No weather data available, please try again later")
    } catch (e: IllegalArgumentException) {
        Result.Error("Invalid request, please try again")
    } catch (e: SocketTimeoutException) {
        Result.Error("Request timed out, please try again")
    } catch (e: IOException) {
        Result.Error("Network error, please check your connection")
    } catch (e: Exception) {
        Result.Error("Unexpected error, please try again")
    }
}