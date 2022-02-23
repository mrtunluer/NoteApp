package com.yks.noteapp.di

import android.app.Application
import androidx.room.Room
import com.yks.noteapp.db.NoteDao
import com.yks.noteapp.db.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDb(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DB_NAME).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: NoteDatabase): NoteDao {
        return db.getNoteDao()
    }

}