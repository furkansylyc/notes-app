package com.example.easynote.Repository;

import com.example.easynote.Model.Notes;

import java.util.List;

public interface NotesCallback {
    void onResponse(List<Notes> notes);
}
