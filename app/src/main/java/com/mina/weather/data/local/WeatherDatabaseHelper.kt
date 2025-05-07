package com.mina.weather.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class WeatherDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "weather.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE today_forecast (
                date TEXT PRIMARY KEY,
                today TEXT,
                lat REAL,
                lng REAL,
                icon TEXT,
                status TEXT,
                temperature INTEGER,
                rain INTEGER,
                wind_speed INTEGER,
                humidity INTEGER
            )
        """)
        db.execSQL("""
            CREATE TABLE incoming_days (
                day TEXT PRIMARY KEY,
                icon TEXT,
                status TEXT,
                high_temp INTEGER,
                low_temp INTEGER
            )
        """)
        db.execSQL("""
            CREATE TABLE hourly_forecast (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT,
                hour TEXT,
                temperature INTEGER,
                icon TEXT
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS today_forecast")
        db.execSQL("DROP TABLE IF EXISTS incoming_days")
        db.execSQL("DROP TABLE IF EXISTS hourly_forecast")
        onCreate(db)
    }
}