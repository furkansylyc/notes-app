package com.example.easynote.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.easynote.Model.Notes;
import com.example.easynote.Repository.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    public NotesRepository repository;
    public LiveData<List<Notes>> getallNotes;
    public LiveData<List<Notes>> yenideneskiye ;
    public LiveData<List<Notes>> eskidenyeniye;
    public LiveData<List<Notes>> getFavouriteNotes;



    public NotesViewModel( Application application) {
        super(application);

         repository = new NotesRepository(application);
         getallNotes= repository.getallNotes;
         yenideneskiye = repository.yenideneskiye;
         eskidenyeniye = repository.eskidenyeniye;
         getFavouriteNotes=repository.getFavouriteNotes;

    }

    public void insertNote(Notes notes){
        repository.insertNotes(notes );
    }

    public void deleteNote(int id){
        repository.deleteNotes(id);
    }

    public void updateNote(Notes notes){
        repository.updateNotes(notes);
    }
}
