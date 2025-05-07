package com.mina.weather.data.utils

class NoCachedDataException : Exception("No cached data available")
class NoDataAvailableException : Exception("No data available (offline and no cached data)")