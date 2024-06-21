package com.alienspace.moviedb.movielist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = true, entities = [
MovieEntity::class,

])
abstract class MovieDatabase:RoomDatabase() {

    abstract val movieDao:MovieDao
}