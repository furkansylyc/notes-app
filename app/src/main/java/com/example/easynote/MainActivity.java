package com.example.easynote;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
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

    FloatingActionButton newNotesButton;
    NotesViewModel notesViewModel;
    RecyclerView notesRecycler;
    NotesAdapter adapter;
    TextView nofilter, yenieski, eskiyeni, fav;
    List<Notes> filterNotesAllList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        newNotesButton = findViewById(R.id.newNotesButton);
        notesRecycler = findViewById(R.id.notesRecycler);

        nofilter = findViewById(R.id.nofilter);
        yenieski = findViewById(R.id.yenieski);
        eskiyeni = findViewById(R.id.eskiyeni);
        fav = findViewById(R.id.fav);

        nofilter.setBackgroundResource(R.drawable.filter_selected);

        nofilter.setOnClickListener( v -> {
            nofilter.setBackgroundResource(R.drawable.filter_selected);
            yenieski.setBackgroundResource(R.drawable.filter_unselected);
            eskiyeni.setBackgroundResource(R.drawable.filter_unselected);
            fav.setBackgroundResource(R.drawable.filter_unselected);
            notesViewModel.getallNotes.observe(this, notes -> {
                notesRecycler.setLayoutManager(new GridLayoutManager(this, 2));
                adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
                notesRecycler.setAdapter(adapter);
            });
        });



        nofilter.setOnClickListener(v -> {
            nofilter.setBackgroundResource(R.drawable.filter_selected);
            yenieski.setBackgroundResource(R.drawable.filter_unselected);
            eskiyeni.setBackgroundResource(R.drawable.filter_unselected);
            fav.setBackgroundResource(R.drawable.filter_unselected);
            notesViewModel.getallNotes.observe(this, notes -> {
                notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
                filterNotesAllList = notes;
                notesRecycler.setAdapter(adapter);
            });
        });
        yenieski.setOnClickListener(v -> {
            yenieski.setBackgroundResource(R.drawable.filter_selected);
            nofilter.setBackgroundResource(R.drawable.filter_unselected);
            eskiyeni.setBackgroundResource(R.drawable.filter_unselected);
            fav.setBackgroundResource(R.drawable.filter_unselected);
            notesViewModel.yenideneskiye.observe(this, notes -> {
                notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
                filterNotesAllList = notes;
                notesRecycler.setAdapter(adapter);

            });

        });
        eskiyeni.setOnClickListener(v -> {
            eskiyeni.setBackgroundResource(R.drawable.filter_selected);
            yenieski.setBackgroundResource(R.drawable.filter_unselected);
            nofilter.setBackgroundResource(R.drawable.filter_unselected);
            fav.setBackgroundResource(R.drawable.filter_unselected);
            notesViewModel.eskidenyeniye.observe(this, notes -> {
                notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
                filterNotesAllList = notes;
                notesRecycler.setAdapter(adapter);

            });

        });
        fav.setOnClickListener(v -> {
            fav.setBackgroundResource(R.drawable.filter_selected);
            nofilter.setBackgroundResource(R.drawable.filter_unselected);
            yenieski.setBackgroundResource(R.drawable.filter_unselected);
            eskiyeni.setBackgroundResource(R.drawable.filter_unselected);


            notesViewModel.getFavouriteNotes.observe(this, notes -> {
                notesRecycler.setLayoutManager(new StaggeredGridLayoutManager( 2,StaggeredGridLayoutManager.VERTICAL));
                adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
                filterNotesAllList = notes;
                notesRecycler.setAdapter(adapter);

            });
        });

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        newNotesButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, insertNotesActivity.class));
        });

        notesViewModel.getallNotes.observe(this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {
                filterNotesAllList = notes;
                setAdapter(notes);
            }
        });
    }


    public void setAdapter(List<Notes> notes) {
        notesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(MainActivity.this, notes, notesViewModel);
        notesRecycler.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_notes,menu);

        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Not Ara");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                NotesFilter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void NotesFilter(String newText) {
        ArrayList<Notes> FilterNames = new ArrayList<>();

        for(Notes notes:this.filterNotesAllList){

            if (notes.notesTitle.toLowerCase().contains(newText.toLowerCase()) ||
                    notes.notesSubtitle.toLowerCase().contains(newText.toLowerCase())) {
                FilterNames.add(notes);
            }

        }
        this.adapter.searchNotes(FilterNames);


    }
}

