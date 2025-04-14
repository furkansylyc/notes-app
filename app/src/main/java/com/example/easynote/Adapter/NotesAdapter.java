package com.example.easynote.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;

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

     NotesViewModel notesViewModel;
     List<Notes> allnotesItem;
     MainActivity mainActivity;
     List<Notes> notes;
     SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;


    public NotesAdapter(@NonNull MainActivity mainActivity, List<Notes> notes, NotesViewModel notesViewModel) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        allnotesItem = new ArrayList<>(notes);
        this.notesViewModel = new ViewModelProvider(mainActivity).get(NotesViewModel.class);
        sharedPreferences = mainActivity.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void searchNotes(List<Notes> filterredName){

        this.notes = filterredName;
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

        Set<String> favoriteIds = new HashSet<>(sharedPreferences.getStringSet("favorite_notes", new HashSet<>()));
        boolean isFavorite = favoriteIds.contains(String.valueOf(note.id));


        holder.favoriteButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.fav);

        holder.favoriteButton.setOnClickListener(v -> {

            note.isFavorite = isFavorite ? 0 : 1; // 0 ise 1 yap, 1 ise 0 yap


            if (note.isFavorite == 1) {
                favoriteIds.add(String.valueOf(note.id));
            } else {
                favoriteIds.remove(String.valueOf(note.id));
            }

            notesViewModel.updateNote(note);

            editor.putStringSet("favorite_notes", favoriteIds);
            editor.apply();

            holder.favoriteButton.setImageResource(note.isFavorite == 1 ? R.drawable.ic_favorite : R.drawable.fav);
        });


        switch (note.notesPriority) {
            case "1":
                holder.notesPriority.setBackgroundResource(R.drawable.green_tag);
                break;
            case "2":
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_tag);
                break;
            case "3":
                holder.notesPriority.setBackgroundResource(R.drawable.red_tag);
                break;
        }


        holder.title.setText(note.notesTitle);
        holder.subtitle.setText(note.notesSubtitle);
        holder.notesDate.setText(note.notesDate);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, updateNotesActivity.class);
            intent.putExtra("id", note.id);
            intent.putExtra("title", note.notesTitle);
            intent.putExtra("subtitle", note.notesSubtitle);
            intent.putExtra("priority", note.notesPriority);
            intent.putExtra("notes", note.notes);
            mainActivity.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return notes.size();
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
