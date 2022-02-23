package com.yks.noteapp.utils

import com.yks.noteapp.model.Note

sealed class ViewState {
    data class Success(val notes: List<Note>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
    object Empty : ViewState()
    object Loading : ViewState()
}