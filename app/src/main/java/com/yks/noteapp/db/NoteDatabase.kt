package com.yks.noteapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yks.noteapp.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {
    companion object{
        const val DB_NAME = "note_db"
    }
    abstract fun getNoteDao(): NoteDao
}