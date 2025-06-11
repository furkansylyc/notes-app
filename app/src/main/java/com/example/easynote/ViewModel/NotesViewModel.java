package com.example.easynote.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.easynote.Model.Notes;
import com.example.easynote.Repository.NotesRepository;

import java.util.List;
import java.util.function.Consumer;

public class NotesViewModel extends AndroidViewModel {

    private final NotesRepository repository;
    private final MutableLiveData<List<Notes>> notesLiveData = new MutableLiveData<>();

    public NotesViewModel(@NonNull Application application) {
        super(application);
        repository = new NotesRepository();
        fetchNotes();
    }

    public LiveData<List<Notes>> getNotes() {
        return notesLiveData;
    }

    public void fetchNotes() {
        repository.getAllNotes(notesLiveData::setValue);
    }

    public void searchNotes(String query) {
        repository.searchNotes(query, notesLiveData::setValue);
    }

    public void getNotesByPriorityDesc(Consumer<List<Notes>> callback) {
        repository.getNotesByPriorityDesc(callback);
    }

    public void getNotesByPriorityAsc(Consumer<List<Notes>> callback) {
        repository.getNotesByPriorityAsc(callback);
    }

    public void fetchFavouriteNotes() {
        repository.getFavouriteNotes(notesLiveData::setValue);
    }

    public void insertNote(Notes note) {
        repository.insertNote(note, insertedNote -> fetchNotes());
    }

    public void updateNote(Notes note) {
        repository.updateNote(note, updatedNote -> fetchNotes());
    }

    public void deleteNote(int id) {
        repository.deleteNote(id, this::fetchNotes);
    }

}
