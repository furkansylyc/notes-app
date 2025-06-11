package com.example.easynote.API;

import com.example.easynote.Model.Notes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface NoteApi {

    @GET("/notes/search")
    Call<List<Notes>> searchNotes(@Query("q") String keyword);

    @GET("/notes")
    Call<List<Notes>> getAllNotes();

    @GET("/notes/priority-desc")
    Call<List<Notes>> getNotesByPriorityDesc();

    @GET("/notes/priority-asc")
    Call<List<Notes>> getNotesByPriorityAsc();

    @GET("/notes/favorites")
    Call<List<Notes>> getFavouriteNotes();

    @Headers("Content-Type: application/json")
    @POST("/notes")
    Call<Notes> insertNote(@Body Notes note);


    @PUT("/notes/{id}")
    Call<Notes> updateNote(@Path("id") int id, @Body Notes note);

    @DELETE("/notes/{id}")
    Call<Void> deleteNote(@Path("id") int id);
}
