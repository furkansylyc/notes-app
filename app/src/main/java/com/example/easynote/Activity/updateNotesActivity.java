package com.example.easynote.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.easynote.Model.Notes;
import com.example.easynote.R;
import com.example.easynote.ViewModel.NotesViewModel;
import com.example.easynote.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class updateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    NotesViewModel notesViewModel;
    String priority ="3";
    String uptitle,upsubtitle,upnotes,uppriority;
    int upid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        upid= getIntent().getIntExtra("id",0);
        uptitle = getIntent().getStringExtra("title");
        upsubtitle = getIntent().getStringExtra("subtitle");
        upnotes = getIntent().getStringExtra("notes");
        uppriority = getIntent().getStringExtra("priority");

        binding.updateTitle.setText(uptitle);
        binding.updateSubtitle.setText(upsubtitle);
        binding.updateNotes.setText(upnotes);

        switch (uppriority) {
            case "1":
                binding.greenTag.setImageResource(R.drawable.done);
                binding.yellowTag.setImageResource(0);
                binding.redTag.setImageResource(0);
                priority = "1";
                break;
            case "2":
                binding.greenTag.setImageResource(0);
                binding.yellowTag.setImageResource(R.drawable.done);
                binding.redTag.setImageResource(0);
                priority = "2";

                break;
            case "3":
                binding.greenTag.setImageResource(0);
                binding.yellowTag.setImageResource(0);
                binding.redTag.setImageResource(R.drawable.done);
                priority = "3";
                break;
        }



        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);


        binding.greenTag.setOnClickListener( v ->{
            binding.greenTag.setImageResource(R.drawable.done);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(0);
            priority="1";

        });
        binding.yellowTag.setOnClickListener( v ->{
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(R.drawable.done);
            binding.redTag.setImageResource(0);
            priority="2";

        });
        binding.redTag.setOnClickListener( v ->{
            binding.greenTag.setImageResource(0);
            binding.yellowTag.setImageResource(0);
            binding.redTag.setImageResource(R.drawable.done);
            priority="3";

        });

        binding.updateNotesButton.setOnClickListener(v -> {

           String title=binding.updateTitle.getText().toString();
           String subtitle=binding.updateSubtitle.getText().toString();
           String notes=binding.updateNotes.getText().toString();
            UpdateNotes(title,subtitle,notes);

        });
    }

    private void UpdateNotes(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("dd/MM/yyyy",date.getTime());

        Notes updateNotes = new Notes();

        updateNotes.notesTitle = title;
        updateNotes.id = upid;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = notes;
        updateNotes.notesPriority=priority;
        updateNotes.notesDate=sequence.toString();
        SharedPreferences sharedPreferences = getSharedPreferences("EasyNotePrefs", MODE_PRIVATE);
        Set<String> favoriteIds = new HashSet<>(sharedPreferences.getStringSet("favorite_notes", new HashSet<>()));

        boolean isFavorite = favoriteIds.contains(String.valueOf(upid));
        if (isFavorite) {
            favoriteIds.add(String.valueOf(upid));
        } else {
            favoriteIds.remove(String.valueOf(upid));
        }

        sharedPreferences.edit().putStringSet("favorite_notes", favoriteIds).apply();

        notesViewModel.updateNote(updateNotes);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_update, (ViewGroup)
                findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.ic_delete) {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(updateNotesActivity.this, R.style.BottomSheetStyle);

            View view = LayoutInflater.from(updateNotesActivity.this)
                    .inflate(R.layout.delete_screen, (LinearLayout) findViewById(R.id.delete_panel));

            sheetDialog.setContentView(view);


            View deletePanel = view.findViewById(R.id.delete_panel);
            deletePanel.setAlpha(0f);
            deletePanel.setTranslationY(300);

            deletePanel.animate()
                    .alpha(1f)
                    .translationY(0)
                    .setDuration(650)
                    .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                    .start();

            sheetDialog.show();

            TextView yes, no;
            yes = view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);

            yes.setOnClickListener(v -> {
                notesViewModel.deleteNote(upid);
                sheetDialog.dismiss();
                finish();
            });

            no.setOnClickListener(v -> {
                deletePanel.animate()
                        .alpha(0f)
                        .translationY(300)
                        .setDuration(400)
                        .setInterpolator(new android.view.animation.AccelerateInterpolator())
                        .withEndAction(sheetDialog::dismiss)
                        .start();
            });
        }
        return true;
    }

}