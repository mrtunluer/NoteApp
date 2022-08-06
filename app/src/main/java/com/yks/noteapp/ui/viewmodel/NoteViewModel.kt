package com.yks.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yks.noteapp.model.Note
import com.yks.noteapp.repo.NoteRepository
import com.yks.noteapp.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepo: NoteRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<ViewState>(ViewState.Loading)
    val state = _state.asStateFlow()

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.getAllNotes().distinctUntilChanged().collect { notes ->
                if (notes.isNotEmpty()) {
                    _state.value = ViewState.Success(notes)
                } else {
                    _state.value = ViewState.Empty
                }
            }
        }
    }

    fun insertNote(title: String, description: String, time: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                title = title,
                description = description,
                time = time
            )
            noteRepo.insertNote(note)
        }
    }

    fun updateNote(id: Int, title: String, description: String, time: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val note = Note(
                id = id,
                title = title,
                description = description,
                time = time
            )
            noteRepo.update(note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepo.deleteNote(id)
        }
    }

}