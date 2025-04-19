package com.example.easynote.Activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.easynote.BroadcastReceiver.ReminderReceiver;
import com.example.easynote.Model.Notes;
import com.example.easynote.R;
import com.example.easynote.ViewModel.NotesViewModel;
import com.example.easynote.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class updateNotesActivity extends AppCompatActivity {

    ActivityUpdateNotesBinding binding;
    NotesViewModel notesViewModel;
    String priority = "3";
    String uptitle, upsubtitle, upnotes, uppriority;
    int upid;

    FloatingActionButton menuButton, alarmButton, updateNotesButton;
    LinearLayout fabMenu;
    boolean isMenuOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        upid = getIntent().getIntExtra("id", 0);
        uptitle = getIntent().getStringExtra("title");
        upsubtitle = getIntent().getStringExtra("subtitle");
        upnotes = getIntent().getStringExtra("notes");
        uppriority = getIntent().getStringExtra("priority");

        menuButton = findViewById(R.id.menuButton);
        alarmButton = findViewById(R.id.alarmButton);
        updateNotesButton = findViewById(R.id.updateNotesButton);
        fabMenu = findViewById(R.id.fabMenu);

        alarmButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                }
            }
            showDateTimePicker();
        });

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

        binding.greenTag.setOnClickListener(v -> {
            updatePriority("1");
        });
        binding.yellowTag.setOnClickListener(v -> {
            updatePriority("2");
        });
        binding.redTag.setOnClickListener(v -> {
            updatePriority("3");
        });

        updateNotesButton.setOnClickListener(v -> {
            String title = binding.updateTitle.getText().toString();
            String subtitle = binding.updateSubtitle.getText().toString();
            String notes = binding.updateNotes.getText().toString();
            UpdateNotes(title, subtitle, notes);
        });

        menuButton.setOnClickListener(v -> toggleFabMenu());
    }

    private void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();

        new DatePickerDialog(updateNotesActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);

            new TimePickerDialog(updateNotesActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                date.set(Calendar.SECOND, 0);

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
                TextView toastText = layout.findViewById(R.id.custom_toast_message);
                toastText.setText("Alarm Ayarlandı!");

                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                String title = binding.updateTitle.getText().toString();
                String notes = binding.updateNotes.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ReminderReceiver.class);
                intent.putExtra("title", title);
                intent.putExtra("message", notes);

                int requestCode = (int) System.currentTimeMillis();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(),
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                } else {
                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            date.getTimeInMillis(),
                            pendingIntent
                    );
                }

            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


    private void updatePriority(String priorityValue) {
        priority = priorityValue;
        binding.greenTag.setImageResource(priority.equals("1") ? R.drawable.done : 0);
        binding.yellowTag.setImageResource(priority.equals("2") ? R.drawable.done : 0);
        binding.redTag.setImageResource(priority.equals("3") ? R.drawable.done : 0);
    }


    private void toggleFabMenu() {
        if (isMenuOpen) {
            fabMenu.animate().alpha(0f).translationY(100).setDuration(300).withEndAction(() -> fabMenu.setVisibility(View.GONE)).start();
            isMenuOpen = false;
        } else {
            fabMenu.setVisibility(View.VISIBLE);
            fabMenu.setAlpha(0f);
            fabMenu.setTranslationY(100);
            fabMenu.animate().alpha(1f).translationY(0).setDuration(300).start();
            isMenuOpen = true;
        }
    }


    private void UpdateNotes(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("dd/MM/yyyy", date.getTime());

        Notes updateNotes = new Notes();
        updateNotes.notesTitle = title;
        updateNotes.id = upid;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = notes;
        updateNotes.notesPriority = priority;
        updateNotes.notesDate = sequence.toString();

        SharedPreferences sharedPreferences = getSharedPreferences("EasyNotePrefs", MODE_PRIVATE);
        Set<String> favoriteIds = new HashSet<>(sharedPreferences.getStringSet("favorite_notes", new HashSet<>()));

        if (favoriteIds.contains(String.valueOf(upid))) {
            favoriteIds.remove(String.valueOf(upid));
        } else {
            favoriteIds.add(String.valueOf(upid));
        }

        sharedPreferences.edit().putStringSet("favorite_notes", favoriteIds).apply();
        notesViewModel.updateNote(updateNotes);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_update, (ViewGroup) findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ic_delete) {
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


