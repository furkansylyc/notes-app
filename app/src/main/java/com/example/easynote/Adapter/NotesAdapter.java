package com.example.easynote.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easynote.Activity.updateNotesActivity;
import com.example.easynote.MainActivity;
import com.example.easynote.Model.Notes;
import com.example.easynote.R;
import com.example.easynote.ViewModel.NotesViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    private  NotesViewModel notesViewModel;
    private final MainActivity mainActivity;
    private List<Notes> notes;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public void setViewModel(NotesViewModel viewModel) {
        this.notesViewModel = viewModel;
    }

    public NotesAdapter(@NonNull MainActivity mainActivity, List<Notes> notes, NotesViewModel notesViewModel) {
        this.mainActivity = mainActivity;
        this.notes = notes != null ? notes : new ArrayList<>();
        this.notesViewModel = notesViewModel;
        this.sharedPreferences = mainActivity.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void searchNotes(List<Notes> filteredNotes) {
        this.notes = filteredNotes != null ? filteredNotes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(mainActivity)
                .inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {
        Notes note = notes.get(position);
        if (note == null) return;

        String noteIdStr = String.valueOf(note.getId());
        Set<String> favoriteIds = new HashSet<>(sharedPreferences.getStringSet("favorite_notes", new HashSet<>()));
        final boolean[] isFavorite = {favoriteIds.contains(noteIdStr)};

        holder.favoriteButton.setImageResource(isFavorite[0] ? R.drawable.ic_favorite : R.drawable.fav);

        holder.favoriteButton.setOnClickListener(v -> {
            isFavorite[0] = !isFavorite[0];
            note.setIsFavorite(isFavorite[0]);

            if (isFavorite[0]) favoriteIds.add(noteIdStr);
            else favoriteIds.remove(noteIdStr);

            notesViewModel.updateNote(note);
            editor.putStringSet("favorite_notes", favoriteIds).apply();

            holder.favoriteButton.setImageResource(isFavorite[0] ? R.drawable.ic_favorite : R.drawable.fav);
        });


        String priority = note.getNotesPriority() != null ? note.getNotesPriority() : "1";
        int priorityRes = R.drawable.green_tag;
        if ("2".equals(priority)) priorityRes = R.drawable.yellow_tag;
        else if ("3".equals(priority)) priorityRes = R.drawable.red_tag;
        holder.notesPriority.setBackgroundResource(priorityRes);

        holder.title.setText(note.getNotesTitle() != null ? note.getNotesTitle() : "");
        holder.subtitle.setText(note.getNotesSubtitle() != null ? note.getNotesSubtitle() : "");
        holder.notesDate.setText(note.getNotesDate() != null ? note.getNotesDate() : "");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, updateNotesActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getNotesTitle());
            intent.putExtra("subtitle", note.getNotesSubtitle());
            intent.putExtra("priority", note.getNotesPriority());
            intent.putExtra("notes", note.getNotes());
            mainActivity.startActivityForResult(intent, 2);
        });
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class notesViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, notesDate;
        View notesPriority;
        ImageButton favoriteButton;

        public notesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesBaslik);
            subtitle = itemView.findViewById(R.id.notesAltBaslik);
            notesDate = itemView.findViewById(R.id.notesDate);
            notesPriority = itemView.findViewById(R.id.notesPriority);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }

}
