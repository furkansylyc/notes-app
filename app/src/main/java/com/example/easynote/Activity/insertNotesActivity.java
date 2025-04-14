package com.example.easynote.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.text.format.DateFormat;

import com.example.easynote.Model.Notes;
import com.example.easynote.R;
import com.example.easynote.ViewModel.NotesViewModel;
import com.example.easynote.databinding.ActivityInsertNotesBinding;

import java.util.Date;

public class insertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    String title, subtitle, notes;
    NotesViewModel notesViewModel;
    String priority = "3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        // Priority selection (Green, Yellow, Red)
        binding.greenTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(R.drawable.done);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(0);
            priority = "1";
        });

        binding.yellowTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(R.drawable.done);
            binding.redTag.setImageResource(0);
            priority = "2";
        });

        binding.redTag.setOnClickListener(v -> {
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(R.drawable.done);
            priority = "3";
        });


        binding.doneNotesButton.setOnClickListener(view -> {
            title = binding.notesBaslik.getText().toString();
            subtitle = binding.notesAltBaslik.getText().toString();
            notes = binding.notesText.getText().toString();


            if (title.isEmpty() || notes.isEmpty()  ) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_warning, findViewById(R.id.toast_layout_root));
                TextView text = layout.findViewById(R.id.toast_text);
                text.setText("Başlık ve not alanı boş olamaz");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } else {
                CreateNotes(title, subtitle, notes);
            }

        });
    }

    private void CreateNotes(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("dd/MM/yyyy", date.getTime());

        Notes notes1 = new Notes();
        notes1.notesTitle = title;
        notes1.notesSubtitle = subtitle;
        notes1.notes = notes;
        Log.d("CreateNotes", "Notes: " + notes);
        notes1.notesPriority = priority;
        notes1.notesDate = sequence.toString();

        Log.d("CreateNotes", "Inserting note: " + notes1.notes);
        notesViewModel.insertNote(notes1);
        Log.d("CreateNotes", "Note inserted successfully!");

        // Show custom toast
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_custom, (ViewGroup)
                findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        // Close the activity
        finish();
    }
}
