package com.example.easynote.Repository;

import android.util.Log;

import com.example.easynote.API.NoteApi;
import com.example.easynote.API.RetrofitClient;
import com.example.easynote.Model.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesRepository {

    private final NoteApi noteApi;

    public NotesRepository() {
        noteApi = RetrofitClient.getRetrofitInstance().create(NoteApi.class);
    }

    public void getAllNotes(Consumer<List<Notes>> callback) {
        noteApi.getAllNotes().enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful()) {
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "getAllNotes failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesRepository", "getAllNotes error", t);
            }
        });
    }

    public void getNotesByPriorityDesc(Consumer<List<Notes>> callback) {
        noteApi.getNotesByPriorityDesc().enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("SIRALI", "Gelen notlar: " + response.body());
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "getNotesByPriorityDesc failed: " + response.code());
                    callback.accept(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesRepository", "getNotesByPriorityDesc error", t);
                callback.accept(new ArrayList<>());
            }
        });
    }


    public void getNotesByPriorityAsc(Consumer<List<Notes>> callback) {
        noteApi.getNotesByPriorityAsc().enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful()) {
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "getNotesByPriorityAsc failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesRepository", "getNotesByPriorityAsc error", t);
            }
        });
    }

    public void getFavouriteNotes(Consumer<List<Notes>> callback) {
        noteApi.getFavouriteNotes().enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful()) {
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "getFavouriteNotes failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesRepository", "getFavouriteNotes error", t);
            }
        });
    }

    public void insertNote(Notes note, Consumer<Notes> callback) {
        noteApi.insertNote(note).enqueue(new Callback<Notes>() {
            @Override
            public void onResponse(Call<Notes> call, Response<Notes> response) {
                if (response.isSuccessful()) {
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "insertNote failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Notes> call, Throwable t) {
                Log.e("NotesRepository", "insertNote error", t);
            }
        });
    }

    public void updateNote(Notes note, Consumer<Notes> callback) {
        Log.d("UpdateRequest", "API'ye gönderiliyor: " + note.getId() + " / " + note.getNotesTitle());
        Log.d("FAVORITE_JSON", new com.google.gson.Gson().toJson(note));
        noteApi.updateNote(note.getId(), note).enqueue(new Callback<Notes>() {
            @Override
            public void onResponse(Call<Notes> call, Response<Notes> response) {
                if (response.isSuccessful()) {
                    callback.accept(response.body());
                } else {
                    Log.e("NotesRepository", "updateNote failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Notes> call, Throwable t) {
                Log.e("NotesRepository", "updateNote error", t);
            }
        });
    }

    public void searchNotes(String keyword, NotesCallback callback) {
        noteApi.searchNotes(keyword).enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(response.body());
                } else {
                    callback.onResponse(new java.util.ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("NotesRepository", "searchNotes error", t);
                callback.onResponse(new java.util.ArrayList<>());
            }
        });
    }

    public void deleteNote(int id, Runnable callback) {
        noteApi.deleteNote(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.run();
                } else {
                    Log.e("NotesRepository", "deleteNote failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("NotesRepository", "deleteNote error", t);
            }
        });
    }

}
