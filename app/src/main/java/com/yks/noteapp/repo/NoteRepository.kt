package com.yks.noteapp.repo

import com.yks.noteapp.db.NoteDatabase
import com.yks.noteapp.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val db: NoteDatabase){
    fun getAllNotes() = db.getNoteDao().getAllNotes()

    suspend fun update(note: Note) = db.getNoteDao().updateNotes(note)

    suspend fun insertNote(note: Note) =  db.getNoteDao().insertNote(note)

    suspend fun deleteNote(id: Int) = db.getNoteDao().deleteNote(id)
}