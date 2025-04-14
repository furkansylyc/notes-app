package com.example.easynote.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.easynote.Model.Notes;

import java.util.List;

@androidx.room.Dao
public interface NotesDao {
    @Query("SELECT * FROM Notes_Database")
    LiveData<List<Notes>>  getAllNotes();

    @Query("SELECT * FROM Notes_Database ORDER BY notes_priority DESC")
    LiveData<List<Notes>>  yenidenEskiye();

    @Query("SELECT * FROM Notes_Database ORDER BY notes_priority ASC")
    LiveData<List<Notes>>  eskidenYeniye();

    @Query("SELECT * FROM Notes_Database WHERE isFavorite = 1")
    LiveData<List<Notes>> getFavouriteNotes();





    @Insert
     void insertNotes(Notes... notes);

    @Query("DELETE FROM Notes_Database WHERE id = :id")
     void deleteNotes(int id);

    @Update
    void updateNotes(Notes notes);
}
