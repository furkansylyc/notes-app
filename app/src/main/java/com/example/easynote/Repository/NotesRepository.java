package com.example.easynote.Repository;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.easynote.Dao.NotesDao;
import com.example.easynote.Database.NotesDatabase;
import com.example.easynote.Model.Notes;

import java.util.List;

public class NotesRepository {

    public NotesDao notesDao;
    public LiveData<List<Notes>> getallNotes;
    public LiveData<List<Notes>> eskidenyeniye;
    public LiveData<List<Notes>> yenideneskiye;
    public LiveData<List<Notes>> getFavouriteNotes;



    public NotesRepository(Application application){
        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        notesDao=database.notesDao();
        getallNotes=notesDao.getAllNotes();
        eskidenyeniye = notesDao.eskidenYeniye();
        yenideneskiye= notesDao.yenidenEskiye();
        getFavouriteNotes=notesDao.getFavouriteNotes();

    }

    public void insertNotes(Notes notes){
        notesDao.insertNotes(notes);
    }

    public void deleteNotes(int id){
        notesDao.deleteNotes(id);
    }

    public void updateNotes(Notes notes){
        notesDao.updateNotes(notes);
    }


}
