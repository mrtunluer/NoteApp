package com.yks.noteapp.db

import androidx.room.*
import com.yks.noteapp.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY time asc")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note ORDER BY time desc")
    fun getAllNotesDesc(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM Note where id = :id")
    suspend fun deleteNote(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotes(notes: Note)

}