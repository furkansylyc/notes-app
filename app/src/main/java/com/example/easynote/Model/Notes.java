package com.example.easynote.Model;

import com.google.gson.annotations.SerializedName;

public class Notes {

    private int id;

    @SerializedName("title")
    private String notesTitle;

    @SerializedName("subtitle")
    private String notesSubtitle;

    @SerializedName("content")
    private String notes;

    @SerializedName("notesDate")
    private String notesDate;

    @SerializedName("priority")
    private String notesPriority;

    @SerializedName("isFavorite")
    private boolean isFavorite;

    public Notes() {}

    public Notes(int id, String notesTitle, String notesSubtitle, String notes, String notesDate, String notesPriority, boolean isFavorite) {
        this.id = id;
        this.notesTitle = notesTitle;
        this.notesSubtitle = notesSubtitle;
        this.notes = notes;
        this.notesDate = notesDate;
        this.notesPriority = notesPriority;
        this.isFavorite = isFavorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNotesTitle() { return notesTitle; }
    public void setNotesTitle(String notesTitle) { this.notesTitle = notesTitle; }

    public String getNotesSubtitle() { return notesSubtitle; }
    public void setNotesSubtitle(String notesSubtitle) { this.notesSubtitle = notesSubtitle; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getNotesDate() { return notesDate; }
    public void setNotesDate(String notesDate) { this.notesDate = notesDate; }

    public String getNotesPriority() { return notesPriority; }
    public void setNotesPriority(String notesPriority) { this.notesPriority = notesPriority; }

    public boolean isFavorite() { return isFavorite; }
    public void setIsFavorite(boolean favorite) { isFavorite = favorite; }
}
