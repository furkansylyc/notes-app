package com.example.easynote;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.easynote.Activity.insertNotesActivity;
import com.example.easynote.Adapter.NotesAdapter;
import com.example.easynote.Model.Notes;
import com.example.easynote.ViewModel.NotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton newNotesButton;
    private NotesViewModel notesViewModel;
    private RecyclerView notesRecycler;
    private NotesAdapter adapter;
    private TextView nofilter, yenieski, eskiyeni, fav;

    private List<Notes> allNotesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupFilterButtons();
        setupNewNoteButton();
        setupNotificationChannel();
    }

    private void initViews() {
        newNotesButton = findViewById(R.id.newNotesButton);
        notesRecycler = findViewById(R.id.notesRecycler);
        nofilter = findViewById(R.id.nofilter);
        yenieski = findViewById(R.id.yenieski);
        eskiyeni = findViewById(R.id.eskiyeni);
        fav = findViewById(R.id.fav);
    }

    private void setupRecyclerView() {
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(MainActivity.this, allNotesList, null); // ViewModel sonra atanacak
        notesRecycler.setAdapter(adapter);
    }

    private void setupViewModel() {
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        adapter.setViewModel(notesViewModel);

        notesViewModel.getNotes().observe(this, notes -> {
            allNotesList = notes != null ? notes : new ArrayList<>();
            allNotesList.removeIf(n -> n == null || n.getNotesTitle() == null);
            adapter.searchNotes(allNotesList);
            highlightFilter(nofilter);
        });

    }

    private void setupFilterButtons() {
        nofilter.setOnClickListener(v -> {
            highlightFilter(nofilter);
            adapter.searchNotes(allNotesList);
        });

        yenieski.setOnClickListener(v -> {
            highlightFilter(yenieski);
            notesViewModel.getNotesByPriorityDesc(notes -> adapter.searchNotes(notes));
        });

        eskiyeni.setOnClickListener(v -> {
            highlightFilter(eskiyeni);
            notesViewModel.getNotesByPriorityAsc(notes -> adapter.searchNotes(notes));
        });


        fav.setOnClickListener(v -> {
            highlightFilter(fav);
            List<Notes> favList = new ArrayList<>();
            for (Notes note : allNotesList) {
                if (note != null && Boolean.TRUE.equals(note.isFavorite())) {
                    favList.add(note);
                }
            }
            adapter.searchNotes(favList);
        });
    }

    private void setupNewNoteButton() {
        newNotesButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, insertNotesActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "not_channel",
                    "Not Hatırlatıcı Kanalı",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void highlightFilter(TextView selected) {
        nofilter.setBackgroundResource(R.drawable.filter_unselected);
        yenieski.setBackgroundResource(R.drawable.filter_unselected);
        eskiyeni.setBackgroundResource(R.drawable.filter_unselected);
        fav.setBackgroundResource(R.drawable.filter_unselected);
        selected.setBackgroundResource(R.drawable.filter_selected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_notes, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Not Ara");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                notesViewModel.searchNotes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    notesViewModel.fetchNotes();
                } else {
                    notesViewModel.searchNotes(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesViewModel.fetchNotes();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            notesViewModel.fetchNotes();
        }
    }
}
